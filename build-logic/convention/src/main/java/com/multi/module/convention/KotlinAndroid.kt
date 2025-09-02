package com.multi.module.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk =  libs.findVersion("projectCompileSdkVersion").get().requiredVersion.toInt()
        defaultConfig.minSdk = libs.findVersion("projectMinSdkVersion").get().requiredVersion.toInt()

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            isCoreLibraryDesugaringEnabled = true
        }
    }

    dependencies {
        "coreLibraryDesugaring"(libs.findLibrary("android.desugarJdkLibs").get())
    }


    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)

            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())

            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-opt-in=kotlin.coroutines.ExperimentalMaterial3Api",
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                    "-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi",
                ),
            )
        }
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}