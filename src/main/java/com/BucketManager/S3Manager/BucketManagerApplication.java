package com.BucketManager.S3Manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.BucketManager.S3Manager")
public class BucketManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BucketManagerApplication.class, args);
    }

}
