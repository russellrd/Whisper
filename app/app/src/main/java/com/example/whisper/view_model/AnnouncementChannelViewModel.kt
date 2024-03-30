package com.example.whisper.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.whisper.model.AnnouncementChannelRepositories
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

private const val TAG = "AnnouncementChannelViewModel"

class AnnouncementChannelViewModel {
    private val _repositories: MutableState<AnnouncementChannelRepositories> = mutableStateOf(
        AnnouncementChannelRepositories(
            announcementChannels = listOf()
        )
    )

    val repositories: MutableState<AnnouncementChannelRepositories> = _repositories

    fun getAnnouncementChannels() {
        val header: HashMap<String, String> = hashMapOf()
        Fuel.get("http://10.0.2.2:4321/announcement/getAll").header(header).responseJson{ _, _, result ->
            Log.d(TAG, result.toString())
            when(result){
                is Result.Failure -> {
                    val ex = result.getException()
                    if(ex.response.statusCode == 404){
                        var tmp = AnnouncementChannelRepositories(announcementChannels = listOf())
                        _repositories.value = tmp
                    }
                }

                is Result.Success -> {
                    val tmp = Json.decodeFromString<AnnouncementChannelRepositories>(result.get().obj().toString())
                    _repositories.value = tmp
                }
            }
        }
    }
}