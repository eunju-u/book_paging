import com.android.build.api.dsl.ApplicationExtension
import com.multi.module.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import com.multi.module.convention.libs

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.configure<ApplicationExtension> {
                defaultConfig.targetSdk = libs.findVersion("projectTargetSdkVersion").get().requiredVersion.toInt()

                configureKotlinAndroid(this)
            }
        }
    }
}