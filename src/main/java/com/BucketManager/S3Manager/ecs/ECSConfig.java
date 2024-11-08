package com.BucketManager.S3Manager.ecs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecs.EcsClient;

@Configuration
public class ECSConfig {

    private final String accessKeyId = "*********";
    private final String secretAccessKeyId = "*********";
    @Bean
    public EcsClient ecsClient() {
        return EcsClient.builder()
                .region(Region.EU_WEST_3)  // Asegúrate de establecer la región correcta
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKeyId)))
                .build();
    }
}
