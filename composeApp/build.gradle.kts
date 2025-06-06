import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.jetbrainsPluginCompose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.multiplatform.resources)
    id("app.cash.sqldelight") version "2.1.0"
}

kotlin {
    applyDefaultHierarchyTemplate()

    //jvm("desktop")

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts("-lsqlite3")
        }
    }
    
    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.material3)

            implementation(libs.androidx.ui.tooling.preview)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)


            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.koin.core)

            implementation(libs.androidx.security.crypto)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.biometric)

            implementation(libs.sqldelight.android.driver)

        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.kotlinx.datetime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.kotlinx.io.core)
            implementation(libs.compose.ui)
            implementation(libs.bundles.ktor)

            implementation(libs.accompanist.systemuicontroller)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.gson)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.bottom.sheet.navigator)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)

            implementation(libs.lifecycle.viewmodel)

            //implementation(libs.multiplatform.settings.no.arg)


            implementation(libs.moko.resource)
            implementation(libs.moko.resource.compose)

            implementation(libs.accompanist.systemuicontroller)

            implementation(libs.kotlinx.datetime)
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)
        }
        nativeMain.dependencies {

        }

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.sqldelight.native)
            }

            // Добавим линковку с system libsqlite3
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
    resourcesPackage.set("uz.hb.b2b") // ← твой пакет
    resourcesClassName.set("SharedRes") // Опционально, по умолчанию MR
    resourcesVisibility.set(dev.icerock.gradle.MRVisibility.Internal) // Опционально, по умолчанию Public
    iosBaseLocalizationRegion.set("en") // Опционально, по умолчанию "en"
    iosMinimalDeploymentTarget.set("11.0") // Опционально, по умолчанию "9.0"
}

//compose.desktop {
//    application {
//        mainClass = "MainKt"
//    }
//}

android {
    namespace = "uz.hb.b2b"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "uz.hb.b2b"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        debug {
            buildConfigField("String", "ENVIRONMENT", "\"dev\"")
        }
        release {
            buildConfigField("String", "ENVIRONMENT", "\"prod\"")
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

    bundle {
        language {
            enableSplit = false
        }
    }
}
dependencies {
    implementation(libs.androidx.material3.android)
}




