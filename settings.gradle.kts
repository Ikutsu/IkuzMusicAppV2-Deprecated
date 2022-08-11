pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { setUrl("https://developer.huawei.com/repo/") }
    }
    resolutionStrategy{
        eachPlugin {
            if (requested.id.getNamespace() == "com.huawei.agconnect") {
                if (requested.id.id == "com.huawei.agconnect.agcp") {
                    useModule("com.huawei.agconnect:agcp:1.7.0.300")
                }
            }
        }
    }
}



rootProject.name = "IkuzMusicApp"
include(":IMAandroidApp")
include(":shared")