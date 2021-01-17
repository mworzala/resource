plugins {
    java
    `java-library`
    maven
}

group = "com.mattworzala"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("org.slf4j", "slf4j-api", "1.7.30")

    implementation("org.jetbrains", "annotations", "20.1.0")
    implementation("com.google.code.gson", "gson", "2.8.6")
}
