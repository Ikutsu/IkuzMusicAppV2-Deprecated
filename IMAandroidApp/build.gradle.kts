@file:Suppress("SpellCheckingInspection")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id ("com.huawei.agconnect")
    kotlin("kapt")
    alias(libs.plugins.agp)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id ("dagger.hilt.android.plugin")
}

apply (plugin = "dagger.hilt.android.plugin")

android {

    compileSdk = 32
    defaultConfig {
        applicationId = "com.ikuz.ikuzmusicapp.android"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "0.0.1"

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
    val localProperties = gradleLocalProperties(rootDir)
    signingConfigs {
        create("release") {
            storeFile = file("IMA.jks")
            keyAlias = localProperties["keyAlias"] as String?
            keyPassword = localProperties["keyPassword"] as String?
            storePassword = localProperties["storePassword"] as String?
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
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

    implementation(libs.compose.destinations.animations.core)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.hilt.dagger)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation (libs.hilt.navigationCompose)

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.media3.common)

    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.guava)

    implementation(libs.accompanist.systemUiController)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.flowlayout)

    testImplementation(libs.test.junit)

    implementation(libs.coil.coil)

    implementation(libs.androidx.constraintlayout)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    implementation(libs.smartToolFactory.sliders)

    implementation(libs.shared.elements)

    implementation(libs.hms.audiokit)
    implementation(libs.hms.agconnect.core)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espressoCore)
    androidTestImplementation(libs.compose.test.ui.testJunit4)
}

kapt {
    correctErrorTypes = true
}