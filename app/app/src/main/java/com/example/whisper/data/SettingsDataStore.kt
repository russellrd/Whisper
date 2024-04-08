package com.example.whisper.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.whisper.model.Language
import com.example.whisper.model.Settings
import com.example.whisper.model.Theme
import kotlinx.coroutines.flow.map

const val SETTINGS_DATASTORE = "settings_datastore"

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_DATASTORE)

// Persisting changes made in the settings to Language and Theme
class SettingsDataStore(private val context: Context) {
    companion object {
        val LANGUAGE = stringPreferencesKey("LANGUAGE")
        val THEME = stringPreferencesKey("THEME")
    }

    suspend fun save(settings: Settings) {
        context.settingsDataStore.edit {
            it[LANGUAGE] = settings.language.code
            it[THEME] = settings.theme.name
        }
    }

    fun get() = context.settingsDataStore.data.map {
        var lang = "en"
        var theme = Theme.LIGHT.name

        if (it[LANGUAGE] != null) lang = it[LANGUAGE]!!
        if (it[THEME] != null) theme = it[THEME]!!

        Settings(
            language = Language.getByCode(lang),
            theme = Theme.valueOf(theme)
        )
    }
}
