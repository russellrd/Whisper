package com.example.whisper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.whisper.model.Auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val AUTH_DATASTORE = "auth_datastore"

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = AUTH_DATASTORE)

class AuthDataStore(private val context: Context) {
    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
        var ID = stringPreferencesKey("ID")
        var TGS_SESSION_KEY = stringPreferencesKey("TGS_SESSION_KEY")
        var TGT = stringPreferencesKey("TGT")
    }

    suspend fun login(auth: Auth) {
        context.authDataStore.edit {
            it[IS_LOGGED_IN] = true
            it[ID] = auth.id
            it[TGS_SESSION_KEY] = auth.tgsSessionKey
            it[TGT] = auth.tgt
        }
    }

    suspend fun logout() = context.authDataStore.edit {
        it.clear()
        it[IS_LOGGED_IN] = false
    }

    fun getAuth(): Flow<Auth> = context.authDataStore.data.map {
        Auth(
            id = it[ID]?:"",
            tgsSessionKey = it[TGS_SESSION_KEY]?:"",
            tgt = it[TGT]?:""
        )
    }

    fun isLoggedIn(): Flow<Boolean> = context.authDataStore.data.map {
        return@map it[IS_LOGGED_IN] ?: false
    }
}