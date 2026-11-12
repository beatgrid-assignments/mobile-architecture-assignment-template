# Architecture

## Overview

```
 ┌──────────────────────┐   ┌──────────────────────┐
 │ Android app          │   │ iOS app              │   UI only: Compose / SwiftUI
 │ (Compose)            │   │ (SwiftUI)            │
 └──────────┬───────────┘   └───────────┬──────────┘
            │      shared view models   │
 ┌──────────▼───────────────────────────▼──────────┐
 │ measurementkit-viewmodel                        │   UI state (StateFlow), UI events -> messages
 ├─────────────────────────────────────────────────┤
 │ BeatKit runtime  (beatkit-foundation)           │   send(message) / subscribe(topic) / state
 │   ├── plugins   (measurementkit-module, ...)    │   business logic, common code
 │   └── providers (interfaces + implementations)  │   I/O and platform specifics (expect/actual)
 └──────────────────────┬──────────────────────────┘
                        │ HTTPS / JSON (Ktor: OkHttp on Android, NSURLSession on iOS)
 ┌──────────────────────▼──────────────────────────┐
 │ java-server: controller -> service -> persistence│  Spring Boot, Hibernate, Flyway, MySQL
 └─────────────────────────────────────────────────┘
```

The apps contain as little logic as possible: they render UI state from a shared view model and forward UI events to
it. Everything else lives in the Kotlin middleware, which is written once and runs on both platforms.

## The BeatKit runtime

The middleware is organised around a small runtime (`beatkit-foundation`). The app creates exactly one
`BeatKitRuntime` (see `MeasurementKitFactory`) from a list of **modules**. A module contributes **plugins** and
**providers**.

| Concept | What it is | Example |
|---|---|---|
| **Message** (`BeatKitMessage<R>`) | A request with a typed result. The *only* way the outside world asks the middleware to do something. | `ServerStatusMessage : BeatKitMessage<ServerStatus>` |
| **Message responder** (`BeatKitMessageResponder`) | A plugin that handles one message type. Exactly one responder per message type. | `ServerStatusPlugin` |
| **Plugin** (`BeatKitPlugin`) | Business logic in common code. Receives life cycle callbacks: `onCreate`, `onService`, `onStateChange`, `onReset`, `handleError`. | `LogoutPlugin` |
| **Provider** (`BeatKitProvider`) | Abstraction of I/O or platform functionality behind an interface. Plugins look providers up in `onCreate` via `context.getProvider(...)`. | `RestProvider`, `PropertyStorageProvider` |
| **State** (`BeatKitState`) | The top level state of the runtime. Plugins change it with `context.updateState(...)`. Entering a state with `shouldReset = true` makes every plugin receive `onReset` (wipe user data). | `MeasurementKitState.LoggedOut / LoggedIn` |
| **Topic** (`BeatKitTopic<T>`) | Publish / subscribe channel, for events that are not a response to a message. | |
| **Availability tokens** | Plugins declare `requirements`; a plugin only becomes *serviced* (and receives messages) once all its required tokens are made available by another plugin with `context.setAvailable(...)`. | e.g. a plugin that requires `"authenticated"` |

### Platform specific code

Providers are the seam between common and platform code. There are three ways to provide a platform specific
implementation:

1. **`expect` / `actual`** in the module itself: the interface and an `expect class` in `commonMain`, the `actual`
   implementations in `androidMain` / `iosMain` (and `jvmMain` for tests).
   Examples: `DefaultPropertyStorageProvider` (SharedPreferences / NSUserDefaults), `createPlatformHttpClient`
   (OkHttp / Darwin).
2. **Injected by the app**: `MeasurementKitFactory.createRuntime(additionalProviders = ...)`. Useful when the
   implementation is easier to write in Swift than through Kotlin/Native interop: implement the (Objective-C exported)
   provider protocol in Swift and pass it in.
3. **Common code** that only depends on multiplatform libraries (e.g. Ktor, kotlinx.serialization).

On Android, providers reach the `Context` through `context.platformContext.applicationContext`.

### Worked example: server status

The footer of the login screen shows the server status. This is the complete path:

1. `measurementkit-model`: `ServerStatus`, the REST model (`@Serializable`).
2. `measurementkit-module`: `ServerStatusMessage : BeatKitMessage<ServerStatus>`.
3. `measurementkit-module`: `ServerStatusPlugin`, a `RestPlugin` that responds to the message by calling
   `restProvider.get("api/v1/status")`.
4. `MeasurementKitModule`: registers the plugin (and the `DefaultRestProvider`).
5. `measurementkit-viewmodel`: `LoginViewModel.refreshServerStatus()` sends the message and maps the result to
   `LoginUiState.serverStatus`.
6. Android `LoginScreen` / iOS `LoginView` render `serverStatus`.
7. Test: `ServerStatusPluginTest` (in `jvmTest`) runs the plugin in a real runtime with a mocked HTTP engine.

On the server side the counterpart is `StatusController` (`GET /api/v1/status`).

### Using the middleware from Swift

The view models live in Kotlin and are exported in `MeasurementKit.framework`. Some things to be aware of:

- Kotlin `StateFlow`s cannot be collected from Swift: the view models expose a `FlowWatcher` (`stateWatcher`), which
  `ObservableViewModel.swift` turns into a SwiftUI `ObservableObject`.
- Kotlin `object`s are accessed via `.shared` (`MeasurementKitFactory.shared`).
- Enum entries are exported in lower case (`MeasurementKitState.LoggedIn` -> `MeasurementKitState.loggedin`).
- `suspend` functions are exported as completion handler / `async` functions, but the view models should hide those.

## Server

The server follows a strict layering, one Maven module per layer:

| Module | Responsibility | May use |
|---|---|---|
| `measurementkit-database` | Flyway migrations (`V<yyyyMMddHHmmss>__<description>.sql`) | |
| `measurementkit-persistence` | JPA entities, Spring Data repositories | `beatkit-core-persistence` |
| `measurementkit-service` | Business logic, transactions (`@Transactional`) | persistence |
| `measurementkit-controller` | REST controllers, request / response DTOs | service |
| `measurementkit-application` | Spring Boot application, configuration (`application.yml`), security | everything |

- The schema is owned by Flyway; Hibernate only validates it (`ddl-auto: validate`).
- Locally the server uses an in-memory H2 database in MySQL mode; production runs MySQL (profile `mysql`, see
  `docker-compose.yml`).
- Security is stateless (`SecurityConfiguration`); `/api/v1/status` and `/api/v1/authentication/**` are public, every
  other endpoint requires authentication (and currently always answers 401, since nothing authenticates requests yet).
- Placeholder endpoints throw `NotImplementedException`, which is mapped to `501 Not Implemented`.
