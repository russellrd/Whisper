package com.example.whisper.view_model

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.whisper.crypto.CryptoSystem.Companion.decryptJSON
import com.example.whisper.data.AuthDataStore
import com.example.whisper.model.ASEncryptedResponse
import com.example.whisper.model.ASRequest
import com.example.whisper.model.ASResponse
import com.example.whisper.model.Auth
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "LoginViewModel"

private const val DESIRED_LIFETIME_MINUTES: Long = 5

class AuthStateViewModel(application: Application) : AndroidViewModel(application) {
    private val authDataStore: AuthDataStore

    val isLoggedIn: StateFlow<Boolean>
    val auth: StateFlow<Auth>

    init {
        val context: Context = getApplication<Application>().applicationContext
        authDataStore = AuthDataStore(context)
        isLoggedIn = authDataStore.isLoggedIn().stateIn(viewModelScope, SharingStarted.Lazily, false)
        auth = authDataStore.getAuth().stateIn(viewModelScope, SharingStarted.Eagerly, Auth())
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun login(id: String, password: String) {
        val zdt = ZonedDateTime.now().plusMinutes(DESIRED_LIFETIME_MINUTES)
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
                    var errorText = ""
                    when(ex.response.statusCode) {
                        -1 -> errorText = "Error: cannot connect to backend"
                        404 -> errorText = "Error: route not found"
                        500 -> errorText = "Error: internal server error"
                    }
                    Toast.makeText(getApplication<Application>().applicationContext, errorText, Toast.LENGTH_LONG).show()
                }

                is Result.Success -> {
                    try {
                        val encryptedResponse = Json.decodeFromString<ASEncryptedResponse>(result.get().obj().toString())
                        val md = MessageDigest.getInstance("MD5")
                        val hashedPassword = md.digest(password.toByteArray()).toHexString()
                        val asResponseJSON = decryptJSON(encryptedResponse.asResponse, hashedPassword)
                        val asResponse = Json.decodeFromString<ASResponse>(asResponseJSON)
                        viewModelScope.launch {
                            authDataStore.login(
                                Auth(
                                    id = id,
                                    tgsSessionKey = asResponse.tgsSessionKey,
                                    tgt = encryptedResponse.tgt
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(getApplication<Application>().applicationContext, "Incorrect Username or Password", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authDataStore.logout()
        }
    }
}