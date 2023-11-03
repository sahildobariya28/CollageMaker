plugins {
    id("com.android.application")
}
android {
    namespace = "com.photo.collagemaker"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.photo.collagemaker"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("org.wysaid:gpuimage-plus:3.0.0")
    implementation("com.karumi:dexter:6.2.1")
    implementation("com.steelkiwi:cropiwa:1.0.3")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation("com.airbnb.android:lottie:3.4.4")
    implementation("com.github.siyamed:android-shape-imageview:0.9.3")
    implementation("com.cepheuen.elegant-number-button:lib:1.0.2")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.isseiaoki:simplecropview:1.1.8")
    implementation("com.github.flipzeus:flipzeus:0.0.1")
    implementation("com.github.skydoves:colorpickerview:2.3.0")

    implementation("com.github.devsideal:VectorChildFinder:1.0.0")
}