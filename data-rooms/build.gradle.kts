plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {

    implementation(project(":data-common-speedcubing"))
    implementation(project(":domain-rooms"))
    implementation(project(":domain-core"))
    implementation(project(":network-core"))

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.squareup.retrofit2.retrofit)
    implementation(libs.squareup.retrofit2.converter.gson)

    implementation(libs.dagger.hilt.core)
    ksp(libs.dagger.hilt.compiler)
}