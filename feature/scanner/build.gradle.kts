plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.tareq.feature.scanner"
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Modules
    implementation(projects.core.designSystem)
    implementation(projects.core.model)
    implementation(projects.core.domain)

    //Hilt
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    //barcode
    implementation(libs.barcode.scanner)

    //lifecycle
    implementation(libs.bundles.lifecycle)


    //Coroutines
    implementation(libs.kotlinx.coroutines)

    //lottie
    implementation(libs.lottie)

    //coil
    implementation(libs.coil)

    //Immutable collections
    implementation(libs.kotlinx.immutable.collections)
}