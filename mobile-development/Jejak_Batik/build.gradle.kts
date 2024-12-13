// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

}

buildscript {
    dependencies {
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath ("com.android.tools.build:gradle:8.1.4")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.4")
    }
}


