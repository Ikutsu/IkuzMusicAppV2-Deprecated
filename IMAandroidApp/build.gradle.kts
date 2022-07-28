@file:Suppress("SpellCheckingInspection")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("kapt")
    alias(libs.plugins.agp)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

apply (plugin = "dagger.hilt.android.plugin")

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.ikuz.ikuzmusicapp.android"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    applicationVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}

dependencies {
    implementation (project(":shared"))
    implementation (libs.androidx.activityCompose)
    implementation (libs.androidx.coreKtx)
    implementation (libs.androidx.lifecycleRuntimeKtx)
    implementation (libs.androidx.activityKtx)
    implementation (libs.material.compose.theme.adapter)
    implementation (libs.androidx.activityKtx)

    implementation (libs.compose.material3)
    implementation (libs.compose.material3WindowSize)

    implementation (libs.compose.animation)
    implementation (libs.compose.foundation)
    implementation (libs.compose.material)
    implementation (libs.compose.runtime)
    implementation (libs.compose.ui)
    debugImplementation (libs.compose.ui.tooling)
    implementation (libs.compose.ui.toolingPreview)
    androidTestImplementation (libs.compose.test.ui.testJunit4)
    implementation (libs.compose.material.icons.extended)


    implementation(libs.androidx.navigationCompose)
    implementation(libs.androidx.navigationFragment)
    implementation(libs.androidx.navigationUi)

    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.hilt.dagger)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation (libs.hilt.navigationCompose)

    implementation (libs.exoPlayer.exoPlayer)
    implementation (libs.exoPlayer.extension.mediasession)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.accompanist.systemUiController)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.pager)
    implementation (libs.accompanist.pager.indicators)

    testImplementation(libs.test.junit)

    implementation(libs.coil.coil)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}

kapt {
    correctErrorTypes = true
}