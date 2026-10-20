## 3.0.0

- **Breaking change**. Updates the minimum supported SDK version to Flutter 3.44/Dart 3.12.
- **Breaking change**. Raises the minimum iOS version to 15.0, the lowest deployment target Xcode 27 accepts.
- Migrates to built-in Kotlin.
- Migrates Android Gradle files to Kotlin DSL and AGP 9.
- Removes the deprecated v1 embedding `registerWith`.
- Migrates `dotenv.gradle` to `dotenv.gradle.kts` using the new AGP variant API. Env values are now applied per build variant, and `buildConfig`/`resValues` are enabled automatically. `dotenv.gradle` still works and forwards to the new script.
- Adds Swift Package Manager support on iOS. Apps using Swift Package Manager need a Run Script build phase that runs `BuildDotenvPlist.rb` (see `doc/IOS.md`). CocoaPods apps keep working without changes.
- Adds an iOS privacy manifest.
- Fixes a crash in `ReadDotEnv.rb` when only the fallback `.env` file exists.
- Adds explicit return types to `loadEnvVariables` (`Future<void>`) and `loadValueForTesting` (`void`).

## 2.0.2

- Update deprecated Ruby code in iOS script.

## 2.0.1

- Update to Gradle 7.3.0.
- Update to Kotlin 1.7.20

## 2.0.0

- **Breaking change**. Gradle 4.0+ is needed on Android. 
- Added support for dart null sound safety

## 1.0.8

- Update Android Plugin
- Fix unable to default to .env

## 1.0.7

- Add method for tests

## 1.0.6

- **Breaking change**. reading the envfile has been moved from /tmp/envfile
  to the root project directory. you need to change your pre-action scripts from
  `echo ".env" > /tmp/envfile ` to `echo ".env" > ${SRCROOT}/.envfile`

* Update documentation for iOS
* Fix Product name

## 1.0.5

- Update documentation for iOS

## 1.0.4

- Use a singleton pattern for FlutterConfig class
- Add some notes to iOS readme.

## 1.0.3

- Use the latest Gradle and Kotlin verision

## 1.0.2

- Improve Documentation and example

## 1.0.1

- Add support for numbers in Android Build Flavors
- Fix - Allowing urls in xcconfig for iOS

## 1.0.0

- Clean up logs
- Add Documetation

## 0.0.1

- Initial version
