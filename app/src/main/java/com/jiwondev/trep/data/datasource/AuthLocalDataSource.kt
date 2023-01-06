package com.jiwondev.trep.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.jiwondev.trep.model.preference.UserPreferences
import com.jiwondev.trep.resource.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AuthLocalDataSource(private val datastore: DataStore<Preferences>) {
    /** 데이터 읽기 **/
    val userPreferencesFlow: Flow<UserPreferences> = datastore.data
        .catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val autoLogin = preferences[PreferencesKeys.AUTO_LOGIN] ?: false
            val userToken = preferences[PreferencesKeys.USER_TOKEN] ?: ""
            val userRefreshToken = preferences[PreferencesKeys.USER_REFRESH_TOKEN] ?: ""
            UserPreferences(autoLogin, userToken, userRefreshToken)
        }
}