/*
 * The MIT License (MIT)
 *
 * Copyright 2025 Crown Copyright (Health Education England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.nhs.tis.trainee.versioncatalog;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;
import static org.testcontainers.utility.DockerImageName.parse;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;

/**
 * Configuration for the required test containers.
 */
@TestConfiguration(proxyBeanMethods = false)
public class ContainerConfiguration {

  /**
   * Create a LocalStack container.
   *
   * @param registry The registry to add localstack properties to.
   * @return The created container.
   */
  @Bean
  LocalStackContainer localStackContainer(DynamicPropertyRegistry registry) {
    LocalStackContainer localStackContainer = new LocalStackContainer(
      parse("localstack/localstack:3"))
      .withServices(SQS);

    registry.add("spring.cloud.aws.region.static", localStackContainer::getRegion);
    registry.add("spring.cloud.aws.credentials.access-key", localStackContainer::getAccessKey);
    registry.add("spring.cloud.aws.credentials.secret-key", localStackContainer::getSecretKey);
    registry.add("spring.cloud.aws.sqs.endpoint",
      () -> localStackContainer.getEndpointOverride(SQS).toString());
    registry.add("spring.cloud.aws.sqs.enabled", () -> true);

    return localStackContainer;
  }

  /**
   * Create a MongoDB container.
   *
   * @return The created container.
   */
  @Bean
  @ServiceConnection
  MongoDBContainer mongoContainer() {
    return new MongoDBContainer(parse("mongo:5"));
  }
}
