plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.hasanbasyildiz.learnconnect"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.hasanbasyildiz.learnconnect"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding =true
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.test:core-ktx:1.6.1")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    // navigation component
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.4")

    //ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.7")
    implementation ("androidx.activity:activity-ktx:1.9.3")

    // circleimageview
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //material3
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha04")

    //
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")


    //media3
    implementation("androidx.media3:media3-exoplayer:1.5.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.5.0")
    implementation("androidx.media3:media3-ui:1.5.0")

//Notification
    implementation ("androidx.core:core:1.15.0")

    implementation ("com.google.code.gson:gson:2.8.9")


    implementation ("com.github.bumptech.glide:glide:4.15.1")


    // AndroidX Test
  //  androidTestImplementation ("androidx.test.ext:junit:1.1.5")
   // androidTestImplementation ("androidx.test:core:1.5.0")
  //  androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    // Mockito
    testImplementation ("org.mockito:mockito-core:4.11.0")
    testImplementation ("org.mockito:mockito-inline:4.11.0")
    androidTestImplementation ("org.mockito:mockito-android:4.11.0")

    // Android Test Runner
    //androidTestImplementation ("androidx.test:runner:1.5.2")

    // Kotlin Coroutines
   // testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation ("org.robolectric:robolectric:4.10.3")



}