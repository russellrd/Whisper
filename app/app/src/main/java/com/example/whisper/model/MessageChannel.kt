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
