package com.highthon.server.global.s3.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.UUID

@Service
class S3Service(
    @Value("\${spring.cloud.aws.s3.bucket}")
    private val bucket: String,
    @Value("\${spring.cloud.aws.s3.region}")
    private val region: String,

    private val s3Client: S3Client,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun uploadFile(dir: String, file: MultipartFile): String {
        val key = "${dir}/${UUID.randomUUID()}"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .contentType(file.contentType)
            .contentLength(file.size)
            .contentDisposition("inline")
            .build()

        logger.info("Uploading ${file.originalFilename} files into $bucket key $key")


        s3Client.putObject(
            putObjectRequest,
//            RequestBody.fromBytes(file.bytes)
            RequestBody.fromBytes(file.bytes)
        )

        return key
    }

    fun getUrl(key: String) = "https://$bucket.s3.$region.amazonaws.com/$key"
}