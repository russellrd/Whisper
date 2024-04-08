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

private const val DESIRED_LIFETIME: Long = 1

// Outline possible app roles
enum class Role(val value: Int) {
    USER(1),
    TEAMLEAD(2),
    ADMIN(3),
}

class CryptoSystem {
    companion object {
        // AES encrypt a string with a key
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

        // AES decrypt a string with a key
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

        // Authenticate user by reaching ticket granting server and service server
        fun authenticate(auth: Auth, role: Role, logout: () -> Unit): Boolean {
            // Create a user auth message for ticket granting server
            val userAuth = UserAuth(
                auth.id,
                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
            )

            // Encrypt UserAuth
            val userAuthJSON = Json.encodeToString(userAuth)
            val userAuthString = encryptJSON(userAuthJSON, auth.tgsSessionKey)

            // Create a request for ticket granting server with a desired lifetime in hours
            val tgsRequest = TGSRequest(
                role.value.toString(),
                ZonedDateTime.now().plusHours(DESIRED_LIFETIME).format(DateTimeFormatter.ISO_INSTANT),
                userAuthString,
                auth.tgt
            )

            // Create body and header for HTTP request
            val body = Json.encodeToString(tgsRequest)
            val header: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

            // Make POST request to ticket granting server
            Fuel.post("http://10.0.2.2:4321/auth/tgs").body(body).header(header).responseJson{ _, _, result ->
                Log.d(TAG, result.toString())
                when(result){
                    is Result.Failure -> {}

                    is Result.Success -> {
                        try {
                            // Try to parse and decrypt response using tgs session key
                            val encryptedResponse = Json.decodeFromString<TGSEncryptedResponse>(result.get().obj().toString())
                            val tgsResponseJSON = decryptJSON(encryptedResponse.tgsResponse, auth.tgsSessionKey)
                            val tgsResponse = Json.decodeFromString<TGSResponse>(tgsResponseJSON)

                            // Create new user auth for service server
                            val userAuth2 = UserAuth(
                                auth.id,
                                ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT)
                            )

                            // Encrypt new UserAuth
                            val userAuthJSON2 = Json.encodeToString(userAuth2)
                            val userAuthString2 = encryptJSON(userAuthJSON2, tgsResponse.serviceSessionKey)

                            // Create a request for service server
                            val ssRequest = SSRequest(
                                userAuthString2,
                                encryptedResponse.st
                            )

                            // Create body and header for HTTP request
                            val body2 = Json.encodeToString(ssRequest)
                            val header2: HashMap<String, String> = hashMapOf("Content-Type" to "application/json")

                            // Make POST request to service server
                            Fuel.post("http://10.0.2.2:4321/auth/ss").body(body2).header(header2).responseJson { _, _, result ->
                                Log.d(TAG, result.toString())
                                when (result) {
                                    is Result.Failure -> {}

                                    is Result.Success -> {
                                        // Try to parse and decrypt response using service session key
                                        val encryptedResponse2 = Json.decodeFromString<SSEncryptedResponse>(result.get().obj().toString())
                                        val serviceAuthJSON = decryptJSON(encryptedResponse2.serviceAuth, tgsResponse.serviceSessionKey)
                                        val serviceAuth = Json.decodeFromString<ServiceAuth>(serviceAuthJSON)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Logout if ticket granting ticket is expired
                            logout()
                        }
                    }
                }
            }
            return true
        }
    }
}
