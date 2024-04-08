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
    // Declare auth datastore
    private val authDataStore: AuthDataStore

    // Declare stateflows
    val isLoggedIn: StateFlow<Boolean>
    val auth: StateFlow<Auth>

    // Initialize datastore and stateflows
    init {
        val context: Context = getApplication<Application>().applicationContext
        authDataStore = AuthDataStore(context)
        isLoggedIn = authDataStore.isLoggedIn().stateIn(viewModelScope, SharingStarted.Lazily, false)
        auth = authDataStore.getAuth().stateIn(viewModelScope, SharingStarted.Eagerly, Auth())
    }

    // Login to app through KDC endpoint to get ticket granting ticket
    @OptIn(ExperimentalStdlibApi::class)
    fun login(id: String, password: String) {
        // Create a request for authentication server with a desired lifetime in minutes
        val zdt = ZonedDateTime.now().plusMinutes(DESIRED_LIFETIME_MINUTES)
        val asRequest = ASRequest(
            id,
            "1.92.168.0.1",
            zdt.format(DateTimeFormatter.ISO_INSTANT)
        )

        // Create body and header for HTTP request
        val body = Json.encodeToString(asRequest)
        val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

        // Make a POST request to login endpoint
        Fuel.post("http://10.0.2.2:4321/auth/login").body(body).header(header).responseJson{ _, _, result ->
            Log.d(TAG, result.toString())
            when(result){
                // Print out errors in a Toast
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
                        // Try to parse and decrypt response using password hash
                        val encryptedResponse = Json.decodeFromString<ASEncryptedResponse>(result.get().obj().toString())
                        val md = MessageDigest.getInstance("MD5")
                        val hashedPassword = md.digest(password.toByteArray()).toHexString()

                        // Parse authentication server response
                        val asResponseJSON = decryptJSON(encryptedResponse.asResponse, hashedPassword)
                        val asResponse = Json.decodeFromString<ASResponse>(asResponseJSON)

                        // Store login information in auth datastore
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
                        // Make toast with error message if password cannot be decrypted
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