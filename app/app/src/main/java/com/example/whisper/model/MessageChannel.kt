package com.example.whisper.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageChannel(
    var id: String = "",
    var user1: String = "",
    var user2: String = ""
)

@Serializable
data class MessageChannelRepositories(
    val data: List<MessageChannel>
)

@Serializable
data class messageInfo(
    var id: String = "",
    var sender: String = "",
    var receiver: String = "",
    var message_channel: String = "",
    var message: String = "",
    var timestamp: String = ""
)

@Serializable
data class MessageRepositories(
    val data: List<messageInfo>
)