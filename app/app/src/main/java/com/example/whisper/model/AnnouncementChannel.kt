package com.example.whisper.model

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementChannel(
    var a_id: Int = 0,
    var title: String = "",
    var department: String = ""
)

@Serializable
data class AnnouncementChannelRepositories(
    val data: List<AnnouncementChannel>,
    val status: String = ""
)
