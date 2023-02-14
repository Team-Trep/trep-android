package com.jiwondev.trep.resource

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import kotlinx.coroutines.Dispatchers
import java.security.MessageDigest
import java.util.Base64.getEncoder

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        KakaoSdk.init(this, "9cc7e2071eda3eb5a155016060c976ad")

        getHashKey()

    }

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trep_preference")
        val coroutineDispatcher = Dispatchers.IO
    }

    private fun getHashKey() {

        var keyHash = Utility.getKeyHash(this)
        Log.e("KeyHash", keyHash.toString())

    }
}