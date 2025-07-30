plugins {
  java
  alias(libs.plugins.spring.boot)
  alias(libs.plugins.spring.dependency.management)
}

group = "uk.nhs.tis.trainee.versioncatalog"
version = "0.0.1"

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

dependencyManagement {
  imports {
    mavenBom(libs.spring.cloud.dependencies.core.get().toString())
    mavenBom(libs.spring.cloud.dependencies.aws.get().toString())
  }
}

dependencies {
  // Spring Boot
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
  implementation("org.springframework.boot:spring-boot-starter-data-redis")
  implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-web")

  implementation(libs.bundles.shedlock.mongo)

  // Lombok
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  // Mapstruct
  implementation(libs.mapstruct.core)
  annotationProcessor(libs.mapstruct.processor)

  // Amazon AWS
  implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs")
  implementation("io.awspring.cloud:spring-cloud-aws-starter-sns")
  implementation(libs.bundles.aws.xray)

  // PDF
  implementation(libs.bundles.pdf.publishing)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
    vendor.set(JvmVendorSpec.ADOPTIUM)
  }
}

testing {
  suites {

    register<JvmTestSuite>("integrationTest") {
      useJUnitJupiter()
      dependencies {
        implementation(project())
        implementation("org.springframework.boot:spring-boot-starter-test")
        implementation("org.springframework.boot:spring-boot-testcontainers")
        implementation("org.testcontainers:localstack")
        implementation("org.testcontainers:mongodb")
      }
    }

    // Include implementation dependencies.
    val integrationTestImplementation by configurations.getting {
      extendsFrom(configurations.implementation.get())
    }
  }
}

tasks.named("check") {
  dependsOn(testing.suites.named("integrationTest"))
}
