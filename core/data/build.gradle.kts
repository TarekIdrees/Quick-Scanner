plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serilization)
}

android {
    namespace = "com.tareq.core.data"
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //Modules
    implementation(projects.core.model)
    implementation(projects.core.designSystem)
    implementation(projects.core.domain)

    //Networking
    implementation(libs.bundles.ktor)

    //Hilt
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)
}