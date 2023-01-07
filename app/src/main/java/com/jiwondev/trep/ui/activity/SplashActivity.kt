package com.jiwondev.trep.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.resource.App.Companion.coroutineDispatcher
import com.jiwondev.trep.resource.App.Companion.dataStore
import com.jiwondev.trep.ui.viewmodel.AuthViewModel
import com.jiwondev.trep.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class SplashActivity : AppCompatActivity() {
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()

        lifecycleScope.launchWhenStarted {
            viewModel.userPreferencesFlow.collect {
                moveActivity(IntroActivity::class.java)
//                when(it.autoLogin) {
//                    true -> {
//                        // TODO : 토큰인증
//                    }
//                    false -> moveActivity(IntroActivity::class.java)
//                }
            }
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(
                AuthRepository(
                    authRemoteDataSource = AuthRemoteDataSource(coroutineDispatcher),
                    authLocalDataSource = AuthLocalDataSource(dataStore)
                )
            )
        )[AuthViewModel::class.java]
    }

    private fun moveActivity(clazz: Class<*>) {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, clazz))
            finish()
        }, 2000)
    }
}