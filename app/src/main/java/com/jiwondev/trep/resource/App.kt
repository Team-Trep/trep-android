package com.jiwondev.trep.resource

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers

class App: Application() {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trep_preference")
        val coroutineDispatcher = Dispatchers.IO
    }
}