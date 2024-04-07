package com.example.whisper.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.whisper.data.SettingsDataStore
import com.example.whisper.model.Settings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsDataStore: SettingsDataStore

    val settings: StateFlow<Settings>

    init {
        val context: Context = getApplication<Application>().applicationContext
        settingsDataStore = SettingsDataStore(context)
        settings = settingsDataStore.get().stateIn(viewModelScope, SharingStarted.Lazily, Settings())
    }

    fun save(settings: Settings) {
        viewModelScope.launch {
            settingsDataStore.save(settings)
        }
    }
}