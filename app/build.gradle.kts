plugins {
    id("com.android.application") version "8.5.0"
    id("org.jetbrains.kotlin.android") version "1.9.20"
}

import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ---- Auto date from system clock ----
val VERSION_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

// ---- Bugfix number from environment variable (GitHub run number) ----
val bugfixNum = (System.getenv("BUGFIX_NUM") ?: "0").toInt()

val versionCodeDate = VERSION_DATE.replace(".", "").toInt()
val versionCodeFinal = versionCodeDate * 10000 + bugfixNum
val versionNameFinal = "$VERSION_DATE+${bugfixNum.toString().padStart(4, '0')}"

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
        buildConfig = true
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    packagingOptions {
        resources {
            excludes += "**/values-night-v8/*"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.cardview:cardview:1.0.0")

    // Compose dependencies
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.material:material-icons-core:1.5.4")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")
}
