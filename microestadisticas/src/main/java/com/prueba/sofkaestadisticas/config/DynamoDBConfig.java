package com.prueba.sofkaestadisticas.config;
import java.net.URI;


import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;


import software.amazon.awssdk.regions.Region;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.prueba.sofkaestadisticas.infraestructure.persistencia.repository")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretKey);
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
    
}
