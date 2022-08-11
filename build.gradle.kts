@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.agp) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.agcp) apply false

}

buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:7.2.2")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven ("https://jitpack.io")
        maven ("https://s01.oss.sonatype.org/content/repositories/snapshots")
        maven ("https://developer.huawei.com/repo/")
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}