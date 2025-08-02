package com.highthon.server.global.s3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3ClientConfig(
    @Value("\${spring.cloud.aws.s3.region}") private val region: String,
) {
    @Bean
    fun s3Client(
        provider: AwsCredentialsProvider,
    ): S3Client {
        val region = Region.of(this.region)

        return S3Client.builder().region(region).credentialsProvider(provider).build()
    }
}