plugins {
    id("com.android.application")
}

android {
    namespace = "astro.sastikjothidam"
    compileSdk = 35

    defaultConfig {
        applicationId = "astro.naranarayanan"
        minSdk = 27
        targetSdk = 35
        versionCode = 45
        versionName = "45.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.github.miteshpithadiya:SearchableSpinner:master")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.google.android.gms:play-services-location:21.0.1")


    implementation("com.daimajia.slider:library:1.1.5@aar")
    implementation("com.nineoldandroids:library:2.4.0")

    implementation("com.squareup.picasso:picasso:2.4.0")

    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.2")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.31")

    implementation("com.google.android.play:app-update:2.1.0")

    implementation("nl.dionsegijn:konfetti-compose:2.0.4")

    implementation("com.razorpay:checkout:1.6.41")
    implementation("commons-codec:commons-codec:1.9")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")

    implementation ("androidx.core:core-ktx:1.12.0")



}