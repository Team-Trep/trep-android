package com.jiwondev.trep.resource

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trep_preference")
        val coroutineDispatcher = Dispatchers.IO
    }
}