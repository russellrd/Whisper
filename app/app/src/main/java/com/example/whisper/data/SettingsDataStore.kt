package com.example.whisper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.whisper.model.Language
import com.example.whisper.model.Settings
import kotlinx.coroutines.flow.map

const val SETTINGS_DATASTORE = "settings_datastore"

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE)

class SettingsDataStore(private val context: Context) {
    companion object {
        val LANGUAGE = stringPreferencesKey("LANGUAGE")
    }

    suspend fun save(settings: Settings) {
        context.settingsDataStore.edit {
            it[LANGUAGE] = settings.language.code
        }
    }

    fun get() = context.settingsDataStore.data.map {
        var lang = "en"
        if (it[LANGUAGE] != null) lang = it[LANGUAGE]!!
        Settings(
            language = Language.getByCode(lang)
        )
    }
}