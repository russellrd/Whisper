package com.example.whisper.crypto

import android.util.Log
import com.example.whisper.model.Auth
import com.example.whisper.model.SSEncryptedResponse
import com.example.whisper.model.SSRequest
import com.example.whisper.model.ServiceAuth
import com.example.whisper.model.TGSEncryptedResponse
import com.example.whisper.model.TGSRequest
import com.example.whisper.model.TGSResponse
import com.example.whisper.model.UserAuth
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

private const val TAG = "CryptoSystem"

private const val IV_SIZE = 16

enum class Role(val value: Int) {
    USER(1),
    TEAMLEAD(2),
    ADMIN(3),
}

class CryptoSystem {
    companion object {
        fun encryptJSON(unencryptedString: String, key: String): String {
            val initializationVector = Random.Default.nextBytes(ByteArray(IV_SIZE))
            val iv = IvParameterSpec(initializationVector)
            val spec = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, spec, iv)
            val cipherText = cipher.doFinal(unencryptedString.toByteArray())
            val encryptedString = ByteArray(IV_SIZE + cipherText.size)
            System.arraycopy(initializationVector, 0, encryptedString, 0, IV_SIZE)
            System.arraycopy(cipherText, 0, encryptedString, IV_SIZE, cipherText.size)
            return Base64.getEncoder().encodeToString(encryptedString)
        }

        fun decryptJSON(encryptedString: String, key: String): String {
            val b64Decoded = Base64.getDecoder().decode(encryptedString)
            val iv = IvParameterSpec(b64Decoded.copyOfRange(0, IV_SIZE))
            val cipherText = b64Decoded.copyOfRange(IV_SIZE, b64Decoded.size)
            val spec = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, spec, iv)
            val json = cipher.doFinal(cipherText)
            return String(json, StandardCharsets.UTF_8)
        }

        fun authenticate(auth: Auth, role: Role): Boolean {
            val userAuth = UserAuth(
                auth.id,
                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
            )

            // Encrypt UserAuth
            val userAuthJSON = Json.encodeToString(userAuth)
            val userAuthString = CryptoSystem.encryptJSON(userAuthJSON, auth.tgsSessionKey)

            val tgsRequest = TGSRequest(
                role.value,
                ZonedDateTime.now().plusHours(1).format(DateTimeFormatter.ISO_INSTANT),
                userAuthString,
                auth.tgt
            )

            Log.d(TAG, ZonedDateTime.now().plusHours(1).format(DateTimeFormatter.ISO_INSTANT))

            val body = Json.encodeToString(tgsRequest)
            val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

            Fuel.post("http://10.0.2.2:4321/auth/tgs").body(body).header(header).responseJson{ _, _, result ->
                Log.d(TAG, result.toString())
                when(result){
                    is Result.Failure -> {
                        val ex = result.getException()
                        if(ex.response.statusCode == 404){
                            // TODO: error handling
                        }
                    }

                    is Result.Success -> {
                        val encryptedResponse = Json.decodeFromString<TGSEncryptedResponse>(result.get().obj().toString())
                        val tgsResponseJSON = decryptJSON(encryptedResponse.tgsResponse, auth.tgsSessionKey)
                        val tgsResponse = Json.decodeFromString<TGSResponse>(tgsResponseJSON)

                        val userAuth = UserAuth(
                            auth.id,
                            ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
                        )

                        // Encrypt UserAuth
                        val userAuthJSON = Json.encodeToString(userAuth)
                        val userAuthString =
                            CryptoSystem.encryptJSON(userAuthJSON, tgsResponse.serviceSessionKey)

                        val ssRequest = SSRequest(
                            userAuthString,
                            encryptedResponse.st
                        )

                        val body = Json.encodeToString(ssRequest)
                        val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

                        Fuel.post("http://10.0.2.2:4321/auth/ss").body(body).header(header).responseJson { _, _, result ->
                            Log.d(TAG, result.toString())
                            when (result) {
                                is Result.Failure -> {
                                    val ex = result.getException()
                                    if (ex.response.statusCode == 404) {
                                        // TODO: error handling
                                    }
                                }

                                is Result.Success -> {
                                    val encryptedResponse = Json.decodeFromString<SSEncryptedResponse>(result.get().obj().toString())
                                    val serviceAuthJSON = decryptJSON(encryptedResponse.serviceAuth, tgsResponse.serviceSessionKey)
                                    val serviceAuth = Json.decodeFromString<ServiceAuth>(serviceAuthJSON)
                                    Log.d(TAG, serviceAuth.serviceId.toString())
                                }
                            }
                        }
                    }
                }
            }
            return true
        }
    }
}
