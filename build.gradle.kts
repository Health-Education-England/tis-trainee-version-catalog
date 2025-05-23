import com.vanniktech.maven.publish.SonatypeHost
import com.vanniktech.maven.publish.VersionCatalog

plugins {
  `version-catalog`
  id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "uk.nhs.tis.trainee"
version = "0.0.6"

catalog {
  versionCatalog {
    from(files("./gradle/libs.versions.toml"))
  }
}

mavenPublishing {
  configure(VersionCatalog())
  publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
  signAllPublications()

  coordinates(artifactId = "version-catalog")

  pom {
    name = "TIS Trainee Version Catalog"
    description = "A version catalog for TIS Trainee services."
    url = "https://github.com/Health-Education-England"

    developers {
      developer {
        name = "NHS England"
      }
    }

    licenses {
      license {
        name = "MIT"
        url = "https://opensource.org/license/mit"
      }
    }

    scm {
      url = "https://github.com/Health-Education-England/tis-trainee-version-catalog"
      connection.set("scm:git:git://github.com/Health-Education-England/tis-trainee-version-catalog.git")
      developerConnection.set("scm:git:ssh://git@github.com/Health-Education-England/tis-trainee-version-catalog.git")
    }
  }
}
