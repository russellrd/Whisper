package com.example.whisper.view_model

import android.util.Log
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.whisper.crypto.CryptoSystem.Companion.decryptJSON
import com.example.whisper.model.ASEncryptedResponse
import com.example.whisper.model.ASRequest
import com.example.whisper.model.ASResponse
import com.example.whisper.model.Auth
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "LoginViewModel"

class AuthStateViewModel : ViewModel(){
    var isLoggedIn by mutableStateOf(false)
    val auth by mutableStateOf(Auth())

    @OptIn(ExperimentalStdlibApi::class)
    fun login(id: String, password: String) {
        isLoggedIn = true
        val zdt = ZonedDateTime.now().plusHours(1)
        val asRequest = ASRequest(
            id,
            "1.92.168.0.1",
            zdt.format(DateTimeFormatter.ISO_INSTANT)
        )

        val body = Json.encodeToString(asRequest)
        val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

        Fuel.post("http://10.0.2.2:4321/auth/login").body(body).header(header).responseJson{ _, _, result ->
            Log.d(TAG, result.toString())
            when(result){
                is Result.Failure -> {
                    val ex = result.getException()
                    if(ex.response.statusCode == 404){
//                        var tmp = AnnouncementChannelRepositories(announcementChannels = listOf())
//                        _repositories.value = tmp
                    }
                }

                is Result.Success -> {
                    val encryptedResponse = Json.decodeFromString<ASEncryptedResponse>(result.get().obj().toString())
                    val md = MessageDigest.getInstance("MD5")
                    val hashedPassword = md.digest(password.toByteArray()).toHexString()
                    val asResponseJSON = decryptJSON(encryptedResponse.asResponse, hashedPassword)
                    val asResponse = Json.decodeFromString<ASResponse>(asResponseJSON)
                    auth.id = id
                    auth.tgsSessionKey = asResponse.tgsSessionKey
                    auth.tgt = encryptedResponse.tgt
                    isLoggedIn = true
                }
            }
        }
    }
}

val AuthState = compositionLocalOf<AuthStateViewModel> { error("No User Auth Context") }