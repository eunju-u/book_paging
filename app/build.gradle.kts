plugins {
    alias(libs.plugins.multi.module.android.application)
    alias(libs.plugins.multi.module.android.hilt)
    alias(libs.plugins.multi.module.android.compose)
}

android {
    namespace = "com.example.book_paging"

    defaultConfig {
        applicationId = "com.example.book_paging"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // 모듈
    implementation(project(":presentation"))
}