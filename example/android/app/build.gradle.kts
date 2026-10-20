plugins {
    id("com.android.application")
    id("dev.flutter.flutter-gradle-plugin")
}

extra["envConfigFiles"] = mapOf(
    "developdebug" to ".env.dev",
    "developrelease" to ".env.dev",
    "stagingdebug" to ".env.staging",
    "stagingrelease" to ".env.staging",
    "productiondebug" to ".env",
    "productionrelease" to ".env"
)

apply(from = "${project(":flutter_config").projectDir}/dotenv.gradle.kts")

android {
    compileSdk = flutter.compileSdkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets {
        named("main") {
            java.srcDirs("src/main/kotlin")
        }
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.byneapp.flutter_config_example"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        getByName("release") {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    flavorDimensions += "flavors"

    productFlavors {
        create("develop") {
            dimension = "flavors"
        }
        create("staging") {
            dimension = "flavors"
        }
        create("production") {
            dimension = "flavors"
        }
    }

    lint {
        disable += "InvalidPackage"
    }

    namespace = "com.byneapp.flutter_config_example"
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

flutter {
    source = "../.."
}
