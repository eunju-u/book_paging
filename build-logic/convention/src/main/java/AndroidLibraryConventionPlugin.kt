import com.android.build.gradle.LibraryExtension
import com.multi.module.convention.ExtensionType
import com.multi.module.convention.configureBuildTypes
import com.multi.module.convention.configureKotlinAndroid
import com.multi.module.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )
                defaultConfig.targetSdk =
                    libs.findVersion("projectTargetSdkVersion").get().requiredVersion.toInt()
            }
        }
    }
}
