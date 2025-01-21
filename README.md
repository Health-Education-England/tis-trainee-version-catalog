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
    mavenLocal() // Only required if testing changes from a local catalog build.
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

## Publishing the Catalog
Publishing the catalog is currently an automated process, with a manual trigger.
The `Publish to Maven Central` workflow will automatically increment the version
based on commit messages following the [Conventional Commit] specification, and
then publish and release the version catalog to Maven Central.

The [changelog](CHANGELOG.md) will be automatically updated to reflect the
commits included in the new published version.

[Conventional Commit]:https://www.conventionalcommits.org/
