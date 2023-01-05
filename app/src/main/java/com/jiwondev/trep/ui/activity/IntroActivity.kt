package com.jiwondev.trep.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.resource.PreferencesKeys
import com.jiwondev.trep.ui.viewmodel.AuthViewModel
import com.jiwondev.trep.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlin.properties.Delegates

class IntroActivity : AppCompatActivity() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "trep_preference")

    private var bool: Boolean by Delegates.notNull()
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val coroutineDispatcher = Dispatchers.IO


        val test: Flow<Boolean> = dataStore.data
            .map { preferences ->
                bool = preferences[PreferencesKeys.AUTO_LOGIN] ?: true
                preferences[PreferencesKeys.AUTO_LOGIN] ?: true
            }

        GlobalScope.launch {
            test.collectLatest {
                Log.d("bool : ", it.toString())
            }
        }


        // TODO : ViewModel 주입방식 생각하기.
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(
                AuthRepository(
                    authRemoteDataSource = AuthRemoteDataSource(coroutineDispatcher),
                    authLocalDataSource = AuthLocalDataSource()
                )
            )
        )[AuthViewModel::class.java]

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.getLogin()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginFlow.collectLatest {
                Log.d("loginFlow :", it.toString())
            }
        }
    }
}