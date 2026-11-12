# MeasurementKit workspace (assignment edition)

A trimmed down version of our mono repo: one Kotlin Multiplatform "middleware" shared by an Android and an iOS app, and
a Java / Spring Boot / Hibernate back-end. The structure (modules, layering, naming) is the same as in the production
workspace; the amount of functionality is not.

**The assignment itself is described in [ASSIGNMENT.md](ASSIGNMENT.md).**

## Layout

| Directory | What | Open with |
|---|---|---|
| `kotlin-client/` | Kotlin Multiplatform middleware (Android, iOS, JVM for tests) | IntelliJ IDEA / Android Studio |
| `android-app/` | Android app (Jetpack Compose), consumes `kotlin-client` as a Gradle composite build | Android Studio |
| `ios-app/` | iOS app (SwiftUI), links `kotlin-client` as `MeasurementKit.framework` | Xcode |
| `java-server/` | Spring Boot 3.5 / Hibernate 6 / Flyway back-end (Maven) | IntelliJ IDEA |
| `docs/` | [Architecture guide](docs/architecture.md) and [architecture decision records](docs/adr/) | |

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

## Prerequisites

- JDK 21 (Gradle 8.14 does not run on JDK 25). The scripts pick JDK 21 via `/usr/libexec/java_home -v 21`.
- Android Studio with an Android SDK (API 36). If the SDK is not at `~/Library/Android/sdk`, update
  `sdk.dir` in `kotlin-client/local.properties` and `android-app/local.properties`.
- Xcode 16+ on an Apple Silicon Mac (the iOS simulator target is `arm64` only).
- Optional: Docker, to run the server against MySQL instead of the in-memory H2 database.

## Getting started

```sh
./build.sh            # builds and tests server, Kotlin client and Android app (add --ios for the iOS app)
./server.sh           # starts the server on http://localhost:8080 (in-memory H2 database)
```

Verify the server: `curl localhost:8080/api/v1/status`.

**Android**: open `android-app/` in Android Studio and run the `app` configuration on an emulator. The emulator reaches
the server on the host via `http://10.0.2.2:8080`.

**iOS**: open `ios-app/MeasurementApp.xcodeproj` in Xcode and run on a simulator. The build phase "Compile Kotlin
Framework" builds the Kotlin middleware with Gradle, so the first build takes a while. The simulator reaches the server
via `http://localhost:8080`.

Both apps show a login screen with a single button, *Login with phone number*, and the server status in the footer
(which proves the app -> middleware -> server round trip works). The button does not do anything yet: that is the
assignment.

### Useful commands

```sh
cd kotlin-client && ./gradlew jvmTest                                   # middleware unit tests (no emulator needed)
cd kotlin-client && ./gradlew :measurementkit-viewmodel:linkDebugFrameworkIosSimulatorArm64
cd android-app   && ./gradlew :app:installDebug                         # install on a running emulator
cd java-server   && ./mvnw verify                                       # server build + tests
docker compose -f java-server/docker-compose.yml up -d && ./server.sh --spring.profiles.active=mysql
```

The H2 console is available at http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:measurementkit`, user `sa`).

## Placeholders

Everything that belongs to the assignment is marked with `TODO(assignment)`:

```sh
grep -rn "TODO(assignment)" --include=*.kt --include=*.java --include=*.swift .
```
