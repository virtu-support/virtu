plugins {
    id("com.android.application") version "8.5.0"
    id("org.jetbrains.kotlin.android") version "1.9.22"
}

// ===== CUSTOM VERSION (NO RESET) =====
val VERSION_DATE = "2026.08.09"   // Change for new releases
val BUGFIX_NUM = 2               // Increment for each bugfix – NEVER RESET

val versionCodeDate = VERSION_DATE.replace(".", "").toInt()
val versionCodeFinal = versionCodeDate * 10000 + BUGFIX_NUM
val versionNameFinal = "$VERSION_DATE+${BUGFIX_NUM.toString().padStart(4, '0')}"
// =======================================

android {
    namespace = "org.virtu.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.virtu.android"
        minSdk = 26
        targetSdk = 35
        versionCode = versionCodeFinal
        versionName = versionNameFinal

        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            versionNameSuffix = "-dev"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}
