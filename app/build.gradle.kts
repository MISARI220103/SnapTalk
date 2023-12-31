@file:Suppress("UNUSED_EXPRESSION")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-android")
}

android {
    namespace = "com.example.snaptalk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.snaptalk"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dataBinding{
        enable = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    dependencies {

        implementation("androidx.core:core-ktx:1.12.0")
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("com.google.android.material:material:1.10.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

        //circular  image view
        implementation("de.hdodenhof:circleimageview:3.1.0")

        //cardview
        androidTestImplementation("androidx.cardview:cardview:1.0.0")

        //FCM
        implementation("com.google.firebase:firebase-messaging:23.2.1")

        //Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

        //Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.6.2")
        implementation("com.squareup.retrofit2:converter-gson:2.6.2")


        //firebase dependencies
        implementation(platform("com.google.firebase:firebase-bom:28.3.1"))  // Update the version to match your Firebase library versions
        implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
        implementation("com.google.firebase:firebase-database:20.2.2")
        implementation("com.google.firebase:firebase-core:21.1.1")
        implementation("com.google.firebase:firebase-auth:22.1.2")
        implementation("com.google.firebase:firebase-storage:20.2.1")
        implementation(platform("com.google.firebase:firebase-bom:32.3.1"))


        //glide library
        implementation("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}