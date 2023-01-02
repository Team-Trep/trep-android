package com.jiwondev.trep.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.viewmodel.AuthViewModel
import com.jiwondev.trep.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "IntroActivity"
class IntroActivity : AppCompatActivity() {
    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val coroutineDispatcher = Dispatchers.IO

        // TODO : ViewModel 주입방식 생각하기.
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(
                AuthRepository(
                    AuthRemoteDataSource(
                        coroutineDispatcher
                    )
                )
            ))[AuthViewModel::class.java]

        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getLogin()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loginFlow.collectLatest {
                Log.d(TAG, "loginFlow : $it")
            }
        }
    }
}