plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.gms.google-services")
//    id("com.google.devtools.ksp")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    kotlin("plugin.serialization")

}

android {
    namespace = "com.example.bodymeasurement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bodymeasurement"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.material3.window.size)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // navigation compose
    implementation(libs.androidx.navigation.compose)

    // coil
    implementation(libs.coil.compose)

    // Dagger Hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation ("com.google.dagger:hilt-android:2.52")
//    ksp ("com.google.dagger:dagger-compiler:2.52")
//    ksp ("com.google.dagger:hilt-compiler:2.52")
    kapt ("com.google.dagger:dagger-compiler:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")

    // firebase
    implementation(libs.firebase.bom)
    implementation(libs.firebase.auth)
    implementation(libs.google.firebase.firestore)

    // splash screen
    implementation(libs.androidx.core.splashscreen)

    //Credential Manager
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

//    // Desugaring
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.1.1")

    // serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.2")

}