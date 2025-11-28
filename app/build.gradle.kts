import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vasthread.webviewtv.demo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vasthread.webviewtv.demo"
        minSdk = 21
        targetSdk = 34
        versionCode = 6
        versionName = "25.11.27"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

//        ndk {
//            abiFilters += listOf("armeabi-v7a")
//        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    applicationVariants.all {
        val buildType = buildType.name // debug/release
        val version = versionName // 1.0.0
        val time = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())

        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            // 格式: MyApp-google-v1.0.0-debug-20241127.apk
            output.outputFileName = "电视家-v${version}-${buildType}-${time}.apk"
        }
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("com.tencent.tbs:tbssdk:44286")
//    implementation(project(":x5core_armeabi_v7a"))
    implementation("com.tencent.bugly:crashreport:latest.release")
    implementation("com.github.JessYanCoding:AndroidAutoSize:v1.2.1")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.apache.commons:commons-lang3:3.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}