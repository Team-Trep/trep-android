package com.jiwondev.trep.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.databinding.ActivityIntroBinding
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.resource.App.Companion.coroutineDispatcher
import com.jiwondev.trep.resource.App.Companion.dataStore
import com.jiwondev.trep.resource.Constant.Companion.SUCCESS
import com.jiwondev.trep.resource.Constant.Companion.U01_404
import com.jiwondev.trep.resource.Constant.Companion.U02_400
import com.jiwondev.trep.resource.PreferencesKeys
import com.jiwondev.trep.resource.Utils
import com.jiwondev.trep.ui.viewmodel.AuthViewModel
import com.jiwondev.trep.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.properties.Delegates


class IntroActivity : BaseActivity<ActivityIntroBinding>({ ActivityIntroBinding.inflate(it)}) {
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        clickListener()
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


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginFlow.collectLatest {
                    when(it?.code) {
                        SUCCESS -> {
                            viewModel.setUserInfo(it.token, it.refreshToken)
                            startActivity((Intent(this@IntroActivity, MainActivity::class.java)))
                            finish()
                        }
                        U01_404, U02_400 -> Toast.makeText(this@IntroActivity, "올바른 정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        null -> Toast.makeText(this@IntroActivity, "서버에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /** UI 이벤트 **/
    private fun clickListener() {
        binding.loginButton.setOnClickListener {
            viewModel.getLogin(
                userId = binding.emailEditText.text.toString(),
                userPassword = binding.passwordEditText.text.toString()
            )
        }

        binding.introSignUpTextView.setOnClickListener {
            startActivity((Intent(this, SignUpActivity::class.java)))
        }

        binding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(Utils.checkEmailRegex(inputResult.toString())) {
                    true -> binding.emailFormatErrorTextView.visibility = View.GONE
                    false -> binding.emailFormatErrorTextView.visibility = View.VISIBLE
                }
            }
        })

        binding.passwordEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(Utils.checkPasswordRegex(inputResult.toString())) {
                    true -> binding.passwordFormatErrorTextView.visibility = View.GONE
                    false -> binding.passwordFormatErrorTextView.visibility = View.VISIBLE
                }
            }
        })

        // 129는 inputType이 password
        // 1은 inputType이 text
        binding.visiblePasswordImageView.setOnClickListener {
            when(binding.passwordEditText.inputType) {
                129 -> binding.passwordEditText.inputType = 1
                1 -> binding.passwordEditText.inputType = 129
            }
        }

        binding.walkThroghtTextView.setOnClickListener {
            startActivity(Intent(this, WalkThroughActivity::class.java))
        }

        binding.forgotTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }
}
