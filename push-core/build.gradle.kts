plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    applyDefaultHierarchyTemplate()
    androidLibrary {
        namespace = "uz.hb.push_core"
        compileSdk = 35
        minSdk = 24

    }

    val xcfName = "push-coreKit"

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                api(libs.koin.core)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.firebase.messaging.ktx)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }

}

