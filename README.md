# Login with phone number

Welcome to the Beatgrid mobile architecture assignment.

## 🎯 Objective

Our panel apps (Android and iOS) let users log in with their phone number: the user enters their number, receives a
code by SMS, enters the code (or the app fills it in automatically) and is logged in. If there is no account for the
number yet, one is created.

This repository is the starting point of that feature. It is a trimmed down version of our mono repo, with the same
module structure: a shared Kotlin Multiplatform middleware, an Android and an iOS app with a login screen that has a
single *Login with phone number* button, and a Java / Spring Boot / Hibernate back-end with a `User` entity and
placeholder endpoints. Nothing of the login flow itself has been designed yet.

**Your job is to design the feature, end to end.** We will work through it together; we are more interested in how
you reason about the trade-offs than in a complete implementation. Writing code is welcome where it helps to make a
design concrete (interfaces, messages, endpoints, entities, a sequence diagram), but not required.

### ✅ What your design should cover

1. **Structure of the client.** How the feature fits into the plugin / provider architecture of the Kotlin middleware
   (see [docs/architecture.md](docs/architecture.md)), so that it is implemented once and behaves the same on Android
   and iOS. Which messages, plugins and providers are there, what is common code and what is platform specific?
   What do the screens / view models look like?
2. **The security protocol.** How the phone number is verified, what the server keeps track of, and how and when an
   account is created.
3. **Abuse.** Every SMS costs real money, and the endpoint that sends one is public. How do you prevent the feature
   from being abused (by bots, by attackers trying to make money off our SMS traffic, by brute forcing the code)?
4. **Abandoned logins.** Users close the app halfway, enter a wrong number, or never receive the SMS. What happens on
   the client and on the server?
5. **SMS auto-fill.** How do we make sure the code from the SMS is filled into the verification code field
   automatically, on both platforms?
6. **Access tokens.** What does the app receive after a successful login, how are requests authenticated, and how
   does the server validate that?
7. **Token refresh.** How does the session stay alive, how are tokens refreshed, and what happens when that fails?
8. **Infrastructure.** How would you set up the server side for this in production (we run on AWS): which service sends
   the SMS messages, what else is needed, and how is it configured?

### 📦 Deliverables

There is no fixed format. Things that tend to work well:

- A diagram of the components and a sequence diagram of the happy flow (Mermaid in a Markdown file is fine).
- An [architecture decision record](docs/adr/) for the most important decisions: what were the options, what did you
  choose and why.
- The skeleton of the design in code: message classes, plugin / provider interfaces, endpoints, DTOs, entities and
  migrations.

Everything that belongs to the assignment is marked with `TODO(assignment)`:

```bash
grep -rn "TODO(assignment)" --include='*.kt' --include='*.java' --include='*.swift' .
```

## 🧩 Components

| Directory | What | Open with |
|---|---|---|
| `kotlin-client/` | Kotlin Multiplatform middleware (Android, iOS, JVM for tests) | IntelliJ IDEA / Android Studio |
| `android-app/` | Android app (Jetpack Compose), includes `kotlin-client` as a Gradle composite build | Android Studio |
| `ios-app/` | iOS app (SwiftUI), links `kotlin-client` as `MeasurementKit.framework` | Xcode |
| `java-server/` | Spring Boot 3.5 / Hibernate 6 / Flyway back-end (Maven) | IntelliJ IDEA |
| `docs/` | [Architecture guide](docs/architecture.md) and [architecture decision records](docs/adr/) | |

**Start with [docs/architecture.md](docs/architecture.md)**: it explains how the middleware works and walks through a
complete example (the server status shown on the login screen), from the server endpoint to the UI.

### Kotlin client modules

| Module | Contents |
|---|---|
| `beatkit-foundation` | The runtime: modules, plugins, message responders, providers, topics, state |
| `beatkit-module` | Generic building blocks, e.g. the `RestProvider` (Ktor) and `RestPlugin` |
| `measurementkit-model` | Serializable REST model shared with the server API |
| `measurementkit-module` | The app's plugins and providers, `MeasurementKitState`, `MeasurementKitFactory` |
| `measurementkit-viewmodel` | Shared view models (used by both apps), produces the iOS framework |

### Server modules

| Module | Contents |
|---|---|
| `beatkit/beatkit-core-persistence` | Generic persistence base classes (`AbstractEntity`) |
| `measurementkit/measurementkit-database` | Flyway migrations |
| `measurementkit/measurementkit-persistence` | Entities (`UserEntity`, `DeviceEntity`, `UserSessionEntity`) and repositories |
| `measurementkit/measurementkit-service` | Services (`UserService`, `AuthenticationService`, `SmsSender`) |
| `measurementkit/measurementkit-controller` | REST controllers and DTOs |
| `measurementkit/measurementkit-application` | Spring Boot application, configuration, security |

### Prerequisites

**macOS on Apple Silicon is required** for this assignment (Xcode is needed for iOS development, and the iOS simulator
target is `arm64` only).

- JDK 21 (Android Studio's bundled JDK works). Gradle 8.14 does not run on JDK 25; the scripts and the Xcode build
  select JDK 21 with `/usr/libexec/java_home -v 21`.
- [Android Studio](https://developer.android.com/studio) with Android SDK (API 36) and Emulator. The SDK is expected
  in `~/Library/Android/sdk` or at `ANDROID_HOME`.
- [Xcode](https://apps.apple.com/app/xcode/id497799835) 16+ with iOS Simulator.
- Optional: [Docker Desktop](https://www.docker.com/products/docker-desktop/), to run the back-end against MySQL
  instead of the in-memory H2 database.

### Backend

```bash
./server.sh
```

This builds the server and starts it on port 8080 with an in-memory H2 database (the schema is created by Flyway).

Verify it's running:

```bash
curl http://localhost:8080/api/v1/status
```

The H2 console is available at http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:measurementkit`, user `sa`).
To use MySQL instead: `docker compose -f java-server/docker-compose.yml up -d && ./server.sh --spring.profiles.active=mysql`.

### Mobile apps

**Android:** Open `android-app/` in Android Studio and run the `app` configuration.

**iOS:** Open `ios-app/MeasurementApp.xcodeproj` in Xcode and run on a simulator. The build phase "Compile Kotlin
Framework" builds the shared Kotlin code with Gradle, so the first build takes a while.

Both apps show the login screen with the server status in the footer, which proves the app -> middleware -> server
round trip works.

### Platform Notes

- **Android:** Base URL for the emulator is `http://10.0.2.2:8080`
- **iOS:** Base URL for the simulator is `http://localhost:8080`

### Useful commands

```bash
./build.sh [--ios]                                     # build and test everything
cd kotlin-client && ./gradlew jvmTest                  # middleware unit tests, no emulator / simulator needed
cd android-app   && ./gradlew :app:installDebug        # install on a running emulator
cd java-server   && ./mvnw verify                      # server build and tests
```
