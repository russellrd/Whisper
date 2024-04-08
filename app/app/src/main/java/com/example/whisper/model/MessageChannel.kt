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
    var channelId: String = "",
    var sender: String = "",
    var receiver: String = "",
    var timestamp: String = "",
    var message: String = "",
)
