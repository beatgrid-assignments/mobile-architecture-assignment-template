#!/bin/sh
# Builds and tests everything. Options:
#   --ios     also build the iOS app (simulator, requires Xcode)
set -e
ROOT="$(cd "$(dirname "$0")" && pwd)"
if [ -x /usr/libexec/java_home ]; then
  JAVA_HOME="$(/usr/libexec/java_home -v 21)"
  export JAVA_HOME
fi

echo "==> Server"
(cd "$ROOT/java-server" && ./mvnw -q -B verify)

echo "==> Kotlin client"
(cd "$ROOT/kotlin-client" && ./gradlew -q jvmTest compileDebugKotlinAndroid)

echo "==> Android app"
(cd "$ROOT/android-app" && ./gradlew -q :app:assembleDebug)

if [ "$1" = "--ios" ]; then
  echo "==> iOS app"
  (cd "$ROOT/ios-app" && xcodebuild -quiet -project MeasurementApp.xcodeproj -scheme MeasurementApp \
    -configuration Debug -sdk iphonesimulator -destination 'generic/platform=iOS Simulator' \
    CODE_SIGNING_ALLOWED=NO build)
fi

echo "==> Done"
