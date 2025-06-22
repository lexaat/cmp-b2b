
plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrainsPluginCompose)
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {
    applyDefaultHierarchyTemplate()

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "uz.hb.b2b.notifications_core"
        compileSdk = 35
        minSdk = 24
    }

    val xcfName = "notifications-coreKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }


    cocoapods {
        version = "1.0"
        summary = "Push notifications module"
        homepage = "https://example.com"
        ios.deploymentTarget = "15.0"
        podfile = project.file("../iosApp/Podfile")
        name = "NotificationsCore"

        framework {
            baseName = "NotificationsCore"
            isStatic = false
            export(project(":notifications-core"))
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlin.stdlib)
                api(libs.compose.runtime)
                api(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.firebase.messaging.ktx)

                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)
                implementation(libs.koin.core)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}


