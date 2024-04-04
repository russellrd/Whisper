package com.example.whisper.model

import kotlinx.serialization.Serializable

data class Auth (
    var id: String = "",
    var tgsSessionKey: String = "",
    var tgt: String = ""
)

@Serializable
data class ASRequest(
    var id: String = "",
    var ip: String = "",
    var desiredLifetime: String = ""
)

@Serializable
data class ASResponse(
    var tgsId: String = "",
    var timestamp: String = "",
    var lifetime: String = "",
    var tgsSessionKey: String = ""
)

@Serializable
data class ASEncryptedResponse(
    var asResponse: String = "",
    var tgt: String = ""
)

@Serializable
data class UserAuth(
    var id: String = "",
    var timestamp: String = ""
)

@Serializable
data class AuthRepository(
    var tgtSessionKey: String = "",
    var tgt: String = ""
)

@Serializable
data class TGSRequest(
    var serviceId: String = "",
    var desiredLifetime: String = "",
    val userAuth: String = "",
    val tgt: String = ""
)

@Serializable
data class TGSResponse(
    var serviceId: String = "",
    var timestamp: String = "",
    var lifetime: String = "",
    var serviceSessionKey: String = ""
)

@Serializable
data class TGSEncryptedResponse(
    var tgsResponse: String = "",
    var st: String = ""
)

@Serializable
data class SSRequest(
    var userAuth: String = "",
    var st: String = ""
)

@Serializable
data class SSEncryptedResponse(
    var serviceAuth: String = ""
)

@Serializable
data class ServiceAuth(
    var serviceId: String = "",
    var timestamp: String = ""
)