import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.jetbrainsPluginCompose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.multiplatform.resources)
    id("org.jetbrains.kotlin.native.cocoapods")
    id("app.cash.sqldelight") version "2.1.0"
}

kotlin {
    applyDefaultHierarchyTemplate()

    cocoapods {
        summary = "Shared KMP library"
        homepage = "https://example.com"
        podfile = project.file("../iosApp/Podfile")
        ios.deploymentTarget = "15.0"
        version = "1.0"
        framework {
            baseName = "shared"
            isStatic = false
        }
    }

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }
    
    sourceSets {

        val androidMain by getting {
            dependencies {
                    implementation(project(":push-core"))
                    implementation(compose.preview)
                    implementation(libs.androidx.activity.compose)
                    implementation(libs.androidx.material3)

                    implementation(libs.ktor.client.okhttp)

                    implementation(libs.koin.android)
                    implementation(libs.koin.androidx.compose)
                    implementation(libs.koin.core)

                    implementation(libs.androidx.security.crypto)
                    implementation(libs.androidx.datastore.preferences)
                    implementation(libs.androidx.biometric)

                    implementation(libs.sqldelight.android.driver)
                }
        }
        val commonMain by getting {
            dependencies {
                api(project(":push-core"))
                implementation(libs.compose.runtime)
                implementation(libs.kotlinx.datetime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.kotlinx.io.core)
                implementation(libs.compose.ui)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.material.icons.extended)

                implementation(libs.accompanist.systemuicontroller)

                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.gson)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.bottom.sheet.navigator)
                implementation(libs.voyager.tab.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.koin)

                implementation(libs.lifecycle.viewmodel)

                implementation(libs.haze)
                implementation(libs.haze.materials)

                implementation(libs.moko.resource)
                implementation(libs.moko.resource.compose)

                implementation(libs.accompanist.systemuicontroller)

                implementation(libs.kotlinx.datetime)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(project(":push-core"))
                implementation(libs.ktor.client.darwin)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.sqldelight.native)
            }
            kotlin.srcDir("src/iosMain/kotlin")
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("b2b.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight"))
            migrationOutputDirectory.set(file("src/commonMain/sqldelight/b2b/migrations"))
            verifyMigrations.set(true)

        }
    }
}

multiplatformResources {
    resourcesPackage.set("uz.hb.shared") // ← твой пакет
    resourcesClassName.set("SharedRes") // Опционально, по умолчанию MR
    resourcesVisibility.set(dev.icerock.gradle.MRVisibility.Internal) // Опционально, по умолчанию Public
    iosBaseLocalizationRegion.set("en") // Опционально, по умолчанию "en"
    iosMinimalDeploymentTarget.set("15.0") // Опционально, по умолчанию "9.0"
}

android {
    namespace = "uz.hb.shared"
    compileSdk = 36

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // оставить 1.8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }

    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            buildConfigField("String", "ENVIRONMENT", "\"dev\"")
        }
        create("staging") {
            dimension = "env"
            buildConfigField("String", "ENVIRONMENT", "\"staging\"")
        }
        create("prod") {
            dimension = "env"
            buildConfigField("String", "ENVIRONMENT", "\"prod\"")
        }
    }
}





