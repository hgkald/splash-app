plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.21"
}

android {
    namespace = "no.uio.ifi.in2000.team22.badeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.team22.badeapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        testOptions {
            unitTests.isReturnDefaultValues = true;
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val versions = object {
        val KTOR = "2.3.9"
        val MAPBOX = "11.2.0"
        val NAVIGATION = "2.7.7"
        val GOOGLE_PLAY_SERVICES = "21.2.0"
        val ACCOMPANIST = "0.32.0" //do not change this unless Compose version is changed!!!!
    }

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // SLF4J:
    implementation("org.slf4j:slf4j-simple:2.0.12")

    // ktor:
    implementation("io.ktor:ktor-client-core:${versions.KTOR}")
    implementation("io.ktor:ktor-client-cio:${versions.KTOR}")
    implementation("io.ktor:ktor-client-content-negotiation:${versions.KTOR}")
    implementation("io.ktor:ktor-serialization-gson:${versions.KTOR}")

    //mapbox
    implementation("com.mapbox.maps:android:${versions.MAPBOX}")
    implementation("com.mapbox.extension:maps-compose:${versions.MAPBOX}")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:6.15.0")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-core:6.15.0")
    implementation("androidx.annotation:annotation:1.7.1")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${versions.NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${versions.NAVIGATION}")

    //google play services
    implementation("com.google.android.gms:play-services-location:${versions.GOOGLE_PLAY_SERVICES}")

    //google accompanist
    /* this dependency cannot use the latest version,
       since this is the latest version that works with v1.5.0 of Jetpack Compose
    */
    //noinspection GradleDependency
    implementation("com.google.accompanist:accompanist-permissions:${versions.ACCOMPANIST}")
}