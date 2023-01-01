package com.jiwondev.trep.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.viewmodel.AuthViewModel
import com.jiwondev.trep.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.Dispatchers

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val coroutineDispater = Dispatchers.IO

        val viewModel: AuthViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(
                AuthRepository(
                    AuthRemoteDataSource(
                        ioDispatcher = coroutineDispater
                    )
                )
            )
        )[AuthViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            viewModel.getLogin()

            viewModel.loginFlow.collect {
                Log.d("it : ", it.toString())
            }
        }
    }
}