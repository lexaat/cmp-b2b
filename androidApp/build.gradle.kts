plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.jetbrainsPluginCompose)
}

android {
    namespace = "uz.hb.b2b"
    compileSdk = 35

    defaultConfig {
        applicationId = "uz.hb.b2b"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21 // или 17
        targetCompatibility = JavaVersion.VERSION_21 // или 17
    }

    kotlinOptions {
        jvmTarget = "21" // или "17"
    }
    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
        }
        create("staging") {
            dimension = "env"
        }
        create("prod") {
            dimension = "env"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":shared"))
    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.androidx.activity.compose) // или 1.10.1
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Kotlin Android extensions
    implementation(libs.androidx.core.ktx)

    // Support for AppCompat and FragmentActivity
    implementation(libs.androidx.appcompat)

    // Optional — для enableEdgeToEdge
    implementation(libs.androidx.core.splashscreen)

    // Koin (если нужен)
    implementation(libs.koin.android)

    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.core.ktx)
}

apply(plugin = "com.google.gms.google-services")