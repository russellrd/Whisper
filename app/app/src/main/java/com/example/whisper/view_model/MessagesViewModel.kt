package com.example.whisper.view_model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.whisper.model.Auth
import com.example.whisper.model.MessageChannelRepositories
import com.example.whisper.model.MessageRepositories
import com.example.whisper.model.messageInfo
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

private const val TAG = "MessagesViewModel"

class MessagesViewModel {
    private val _repositories: MutableState<MessageRepositories> = mutableStateOf(
        MessageRepositories(
            data = listOf()
        )
    )

//to store Dm messages that can be pulled to display chat history
    val repositories: MutableState<MessageRepositories> = _repositories

    //When send button is clicked, logic to send message to messages DB
    fun sendMessage(sender: String, receiver: String, message_channel: String, message: String) {

        var messageInfo = messageInfo(sender = sender, receiver = receiver, message_channel = message_channel, message = message)
        val body = Json.encodeToString(messageInfo)
        val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")
        Fuel.post("http://10.0.2.2:4321/message/sendMessage").body(body).header(header).responseJson{ _, _, result ->
            Log.d(TAG, result.toString())
            when(result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if(ex.response.statusCode == 404){
                        //var tmp = MessageChannelRepositories(data = listOf())
                        //_repositories.value = tmp
                    }
                }
                //if message is send to DB successfully, display message on screen real time
                is Result.Success -> {
                    /*val messages = _repositories.value.data.toMutableList()
                    messages.add(messageInfo)
                    _repositories.value = MessageRepositories(data = messages)
                    Log.d("abc", )*/
                    /*val new: MutableState<MessageRepositories> = mutableStateOf(MessageRepositories(data=listOf(messageInfo)))
                    //val new = MessageRepositories(data=listOf(messageInfo))
                    _repositories.value = _repositories.value + new.value
                    //val tmp = Json.decodeFromString<MessageChannelRepositories>(result.get().obj().toString())
                    //_repositories.value = tmp*/
                }
            }
        }
    }


    //Pull DM chat history from DB and display when opening a specific DM
    fun getChatHistory(message_channel: String) {
        val header: HashMap<String, String> = hashMapOf()
        Log.d(TAG, message_channel.toString())
        Fuel.get("http://10.0.2.2:4321/message/getChatHistory?id=$message_channel").header(header).responseJson{ _, _, result ->
            Log.d(TAG, result.toString())
            when(result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    if(ex.response.statusCode == 404){
                        var tmp = MessageRepositories(data = listOf())
                        _repositories.value = tmp
                    }
                }

                is Result.Success -> {
                    val tmp = Json.decodeFromString<MessageRepositories>(result.get().obj().toString())
                    _repositories.value = tmp
                }
            }
        }
    }

}