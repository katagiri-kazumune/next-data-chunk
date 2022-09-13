plugins {
    java
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.4")
}

val javaLanguageVersion: JavaLanguageVersion = JavaLanguageVersion.of(11)

java {
    toolchain {
        languageVersion.set(javaLanguageVersion)
    }
}
