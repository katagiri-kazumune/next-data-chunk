plugins {
    java
}

tasks {
    compileJava {
        val aptOptions = extensions.getByType<com.diffplug.gradle.eclipse.apt.AptPlugin.AptOptions>()
        aptOptions.processorArgs = mapOf(
            "doma.domain.converters" to "jp.classmethod.aws.metropolis.sample.DomainConverterProvider"
        )
    }
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
