package com.jiwondev.trep.resource

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val AUTO_LOGIN = booleanPreferencesKey("auto_login")
    val USER_TOKEN = stringPreferencesKey("user_token")
    val USER_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
}