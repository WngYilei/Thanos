plugins {
    id("com.android.application")
    id("kotlin-android")
    id("thanos-plugin")
//    id("thanos")
}
apply<com.xl.plugin.apk.ApkPlugin>()
android {
    compileSdk = 30

    signingConfigs {
        create("release") {
            storeFile = file("enjoy.keystore")
            storePassword = "123456"
            keyAlias = "enjoy"
            keyPassword = "123456"
        }
    }


    defaultConfig {
        applicationId = "com.xl.fast"
        minSdk = 27
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.1"
        kotlinCompilerVersion = "1.5.21"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("debug") {
            jniLibs.srcDir("libs")
        }
        getByName("release") {
            java.srcDir("libs")
        }
    }
}

dependencies {
    val compose_version = "1.0.1"
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.3.0-alpha06")
    implementation("junit:junit:4.+")
    implementation("androidx.test.ext:junit:1.1.2")
    implementation("androidx.test.espresso:espresso-core:3.3.0")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")
}
repositories {
    mavenCentral()
}
