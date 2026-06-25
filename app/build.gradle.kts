plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.example.picobotella"

  compileSdk = 36

  defaultConfig {
    applicationId = "com.example.picobotella"
    minSdk = 24
    targetSdk = 36
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
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  buildFeatures {
    dataBinding = true
  }
}

kotlin {
  jvmToolchain(21)
}

dependencies {

  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.core.ktx)
  implementation(libs.material)
  implementation(libs.androidx.fragment.ktx)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)

  implementation("androidx.activity:activity-ktx:1.10.1")
  implementation("androidx.navigation:navigation-common-ktx:2.9.3")
  implementation("androidx.recyclerview:recyclerview:1.4.0")
  implementation("androidx.cardview:cardview:1.0.0")

  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

  implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
  implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")

  implementation("androidx.room:room-runtime:2.7.2")
  implementation("androidx.room:room-ktx:2.7.2")
  ksp("androidx.room:room-compiler:2.7.2")

  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")

  implementation("com.github.bumptech.glide:glide:4.16.0")

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}