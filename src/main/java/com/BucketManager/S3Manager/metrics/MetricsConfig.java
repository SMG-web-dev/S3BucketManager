package com.BucketManager.S3Manager.metrics;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
public class MetricsConfig {

    private final String accessKeyId = "*********";
    private final String secretAccessKeyId = "*********";
    private final String namespace = "S3ManagerMetrics";
    private final String step = "PT1M";

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config, CloudWatchAsyncClient client) {
        return new CloudWatchMeterRegistry(config, Clock.SYSTEM, client);
    }

    @Bean
    public CloudWatchConfig cloudWatchConfig() {
        return key -> {
            switch (key) {
                case "cloudwatch.namespace":
                    return namespace;
                case "cloudwatch.step":
                    return step;
                default:
                    return null;
            }
        };
    }

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .region(Region.EU_WEST_3)  // Asegúrate de establecer la región correcta
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKeyId)))
                .build();
    }
}
