package com.example.whisper.model

import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementChannel(
    var id: String = "",
    var title: String = "",
    var department: String = ""
)

@Serializable
data class AnnouncementChannelRepositories(
    val announcementChannels: List<AnnouncementChannel>
)