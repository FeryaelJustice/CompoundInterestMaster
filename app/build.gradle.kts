/*
 * Copyright (c) 2026 Feryael Justice. Todos los derechos reservados.
 */

import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}


val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(FileInputStream(file))
    }
}

val testAdmobAppId = "ca-app-pub-3940256099942544~3347511713"
val testInterstitialAdUnitId = "ca-app-pub-3940256099942544/1033173712"

val debugAdmobAppId = localProperties.getProperty("admob.appId.test")?.trim()
    ?.takeIf { it.isNotEmpty() } ?: testAdmobAppId

val releaseAdmobAppId = localProperties.getProperty("admob.appId.production")?.trim()
    ?.takeIf { it.isNotEmpty() }
    ?: localProperties.getProperty("admob.appId")?.trim()
    ?.takeIf { it.isNotEmpty() } ?: testAdmobAppId

val debugInterstitialAdUnitId = localProperties.getProperty("admob.interstitial.testAdUnitId")?.trim()
    ?.takeIf { it.isNotEmpty() } ?: testInterstitialAdUnitId

val releaseInterstitialAdUnitId = localProperties.getProperty("admob.interstitial.productionAdUnitId")?.trim()
    ?.takeIf { it.isNotEmpty() } ?: ""

android {
    namespace = "com.feryaeljustice.compoundinterestmaster"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.feryaeljustice.compoundinterestmaster"
        minSdk = 24
        targetSdk = 37
        versionCode = 5
        versionName = "1.2.0"

        testInstrumentationRunner = "com.feryaeljustice.compoundinterestmaster.HiltTestRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            manifestPlaceholders["admobAppId"] = debugAdmobAppId
            buildConfigField(
                "String",
                "ADMOB_INTERSTITIAL_AD_UNIT_ID",
                "\"${debugInterstitialAdUnitId.replace("\"", "\\\"")}\""
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            manifestPlaceholders["admobAppId"] = releaseAdmobAppId
            buildConfigField(
                "String",
                "ADMOB_INTERSTITIAL_AD_UNIT_ID",
                "\"${releaseInterstitialAdUnitId.replace("\"", "\\\"")}\""
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.google.android.material)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.androidx.runner)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    ksp(libs.hilt.android.compiler)
    ksp(libs.hilt.compiler)

    kspTest(libs.hilt.compiler)
    kspAndroidTest(libs.hilt.android.compiler)

    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
    implementation(libs.play.services.ads)
    implementation(libs.user.messaging.platform)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}

