package com.highthon.server.global.oauth.model.dto.response

data class OIDCPublicKeysResponse(
    val keys: List<Keys>
) {
    data class Keys(
        val kty: String,
        val use: String,
        val kid: String,
        val alg: String,
        val n: String,
        val e: String
    )
}
