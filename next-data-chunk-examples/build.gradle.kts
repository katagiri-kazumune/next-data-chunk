plugins {
    java
}

dependencies {
    val domaVersion: String by project
    annotationProcessor("org.seasar.doma:doma-processor:${domaVersion}")
    implementation("org.seasar.doma:doma-core:${domaVersion}")
    implementation("org.seasar.doma:doma-slf4j:${domaVersion}")

    implementation(project(":metropolis-data-chunk"))
}

val javaLanguageVersion: JavaLanguageVersion = JavaLanguageVersion.of(11)

java {
    toolchain {
        languageVersion.set(javaLanguageVersion)
    }
}
