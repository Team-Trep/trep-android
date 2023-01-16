package com.jiwondev.trep.ui.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jiwondev.trep.R
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.databinding.ActivitySignUpBinding
import com.jiwondev.trep.resource.App
import com.jiwondev.trep.resource.App.Companion.dataStore
import com.jiwondev.trep.ui.viewmodel.AuthViewModel
import com.jiwondev.trep.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SignUpActivity : BaseActivity<ActivitySignUpBinding>({ActivitySignUpBinding.inflate(it)}) {
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        clickListener()
        subscribeUi()
    }

    private fun clickListener() {
        binding.emailValidationButton.setOnClickListener {
            // viewModel.getSendEmail(binding.emailEditTextView.text.toString())

            // 200일 때
            binding.authCodeConstraint.visibility = View.VISIBLE
            binding.emailValidationButton.isEnabled = false
            startTimer()


        }

        binding.backButton.setOnClickListener {

        }

        binding.codeValidationButton.setOnClickListener {

        }

        binding.emailResendTextView.setOnClickListener {
            // viewModel.getSendEmail(binding.emailEditTextView.text.toString())
            // 3분 이전에도 보낼 수 있는지?
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(
                AuthRepository(
                    authRemoteDataSource = AuthRemoteDataSource(App.coroutineDispatcher),
                    authLocalDataSource = AuthLocalDataSource(dataStore)
                )
            )
        )[AuthViewModel::class.java]

        binding.emailResendTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun subscribeUi() {
        viewModel.sendEmailLiveData.observe(this) { code ->
            when(code) {
                200 -> binding.authCodeConstraint.visibility = View.VISIBLE
                400 -> Toast.makeText(this, "이메일 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTimer() {
        // 3분
        viewModel.countDownTimer = object : CountDownTimer(180000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("millisUntilFinished : ", millisUntilFinished.toString())
                viewModel.time--
                binding.minuteTextView.text = "0${viewModel.time/60}:"
                when {
                    viewModel.time%60 >= 10 -> binding.secondTextView.text = "${viewModel.time%60}"
                    else -> binding.secondTextView.text = "0${viewModel.time%60}"
                }
            }

            override fun onFinish() {
                viewModel.countDownTimer?.cancel()
            }
        }
        viewModel.countDownTimer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.countDownTimer?.cancel()
    }
}