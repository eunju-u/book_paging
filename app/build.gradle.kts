plugins {
    alias(libs.plugins.multi.module.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.example.book_paging"

    defaultConfig {
        applicationId = "com.example.book_paging"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // 모듈
    implementation(project(":presentation"))
}