package com.highthon.server.global.oauth.properties

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("kakao")
data class KakaoProperties(
    val nativeAppKey: String,
    val restApiKey: String,
    val javascriptKey: String,
    val adminKey: String,
) {
    val appKeys: List<String> = listOf(
        nativeAppKey,
        restApiKey,
        javascriptKey,
        adminKey
    )
}