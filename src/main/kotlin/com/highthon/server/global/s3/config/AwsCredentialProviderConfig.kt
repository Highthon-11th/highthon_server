package com.highthon.server.global.s3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider

@Configuration
class AwsCredentialProviderConfig(
    @Value("\${spring.cloud.aws.credentials.access-key}")
    private val accessKey: String,
    @Value("\${spring.cloud.aws.credentials.secret-key}")
    private val secretKey: String,
) {
    @Bean
    fun awsCredentialsProvider(): AwsCredentialsProvider {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        return AwsCredentialsProvider { credentials }
    }
}