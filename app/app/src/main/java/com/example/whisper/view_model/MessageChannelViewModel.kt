package com.example.whisper.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.whisper.model.Auth
import com.example.whisper.model.MessageChannelRepositories
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

private const val TAG = "MessageChannelViewModel"

class MessageChannelViewModel { // I feel like this class can be abstracted to since the view model is the only thing that changes - Kalp
    private val _repositories: MutableState<MessageChannelRepositories> = mutableStateOf(
        MessageChannelRepositories(
            data = listOf()
        )
    )

    val repositories: MutableState<MessageChannelRepositories> = _repositories

    fun getMessageChannels(auth: Auth) {
        val header: HashMap<String, String> = hashMapOf()

        // The id value here is hard-coded to 1 since that's Kalp Shah's user id
            // We need a global variable for the current user's ID and that can be plugged in here
        val userID = auth.id; // Change this value to different user id's to see the message list change
        Fuel.get("http://10.0.2.2:4321/messageChannel/getCurrentChats?id=$userID").header(header).responseJson{ _, _, result ->
            Log.d(TAG, result.toString())
            when(result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if(ex.response.statusCode == 404){
                        var tmp = MessageChannelRepositories(data = listOf())
                        _repositories.value = tmp
                    }
                }

                is Result.Success -> {
                    val tmp = Json.decodeFromString<MessageChannelRepositories>(result.get().obj().toString())
                    _repositories.value = tmp
                }
            }
        }
    }
}