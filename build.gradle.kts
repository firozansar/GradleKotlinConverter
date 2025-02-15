plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt("))
    id("dagger.hilt.android.plugin")
}

repositories {
    mavenCentral()
    google()

    val localProperties = new Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.newInputStream())
    }
    maven {
        url = uri("https://maven.pkg.github.com/ComparetheMarket/mobile.user-session.android")
        credentials {
            username = localProperties.getProperty("gpr.user")
            password = localProperties.getProperty("gpr.key")
        }
    }
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.comparethemarket.library.usersession.demo"
        minSdk = 23
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = new Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.newInputStream())
        }

        buildConfigField("String", "CLIENT_ID", localProperties.getProperty("clientId"))
        buildConfigField("String", "CLIENT_SECRET", localProperties.getProperty("clientSecret"))

        buildConfigField("String", "PIN_CLIENT_ID", localProperties.getProperty("pinClientId"))
        buildConfigField("String", "PIN_CLIENT_SECRET", localProperties.getProperty("pinClientSecret"))

        buildConfigField("String", "SSO_SECRET", localProperties.getProperty("ssoSecret"))
        buildConfigField("String", "SSO_CLIENT_ID", localProperties.getProperty("ssoClientId"))

        buildConfigField("String", "USERNAME", localProperties.getProperty("username"))
        buildConfigField("String", "PASSWORD", localProperties.getProperty("password"))
    }

    buildTypes {
        named("debug") {
            isMinifyEnabled = true
        }
        named("release") {
            isMinifyEnabled = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    flavorDimensions("async")
    productFlavors {
        create("coroutines") {
            dimension = "async"
            applicationIdSuffix = ".coroutines"
            manifestPlaceholders = listOf(appLabel = "UserSession Coroutines Demo")
        }
        create("rxjava3") {
            dimension = "async"
            applicationIdSuffix = ".rxjava3"
            manifestPlaceholders = listOf(appLabel = "UserSession RxJava3 Demo")
        }
    }
    compileOptions {
        coreLibraryDesugaringEnabled = true

        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        useIR = true
    }
    buildFeatures {
        compose true
    }
    composeOptions {
        kotlinCompilerExtensionVersion compose_version
        kotlinCompilerVersion "1.6.10"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.navigation:navigation-compose:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.1")

    val dagger = "2.40.5"

    implementation("com.google.dagger:dagger:$dagger")
    kapt("com.google.dagger:dagger-compiler:$dagger")
    implementation("com.google.dagger:hilt-android:$dagger")
    kapt("com.google.dagger:hilt-compiler:$dagger")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.1.0")

    // common-global
    implementation("com.comparethemarket.library:common-global:0.3.4")
    // User-session
    // Use if you want to use user-session-coroutines from Maven
    // coroutinesImplementation "com.comparethemarket.library:user-session-coroutines:0.1.0"

    // Use if you want to use user-session-rxjava3 from Maven
    // rxjava3Implementation "com.comparethemarket.library:user-session-rxjava3:0.1.0"

    // Use if you want to use the local user-session module. It will automatically select
    // coroutines or rxjava3 depending on the build flavor you have selected in AS.
    implementation(project(":user-session"))

    // RxJava3
    rxjava3Implementation "io.reactivex.rxjava3:rxjava:3.1.3"
    rxjava3Implementation "io.reactivex.rxjava3:rxandroid:3.0.0"
}
