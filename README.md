# TIS Trainee Version Catalog
A dependency version catalog for trainee services.

## Building the Catalog
The catalog can be built locally using the `publishToMavenLocal` task.

## Using the Catalog
First, add the catalog to the Gradle settings file (e.g. `settings.gradle.kts`).

```kotlin
dependencyResolutionManagement {
  repositories {
    mavenCentral()
    mavenLocal() // Required until publishing to Maven Central is implemented.
  }

  versionCatalogs {
    create("libs") {
      from("uk.nhs.tis.trainee:tis-trainee-version-catalog:0.0.1")
    }
  }
}
```

The catalog can then be used when declaring dependencies.

```kotlin
dependencyManagement {
  imports {
    // Import a Maven BOM with the version managed by the catalog.
    mavenBom(libs.spring.cloud.dependencies.aws.get().toString())
  }
}

dependencies {
  // Import a single dependency.
  implementation(libs.mapstruct.core)
  annotationProcessor(libs.mapstruct.processor)

  // Import a bundle of related dependencies that are usually used together.
  implementation(libs.bundles.pdf.publishing)
  implementation(libs.bundles.mongock)
}
```
