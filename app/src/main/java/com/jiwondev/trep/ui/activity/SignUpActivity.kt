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
import com.jiwondev.trep.resource.Constant.Companion.E01_500
import com.jiwondev.trep.resource.Constant.Companion.E02_400
import com.jiwondev.trep.resource.Constant.Companion.E03_400
import com.jiwondev.trep.resource.Constant.Companion.SUCCESS
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
    }

    private fun clickListener() {
        binding.emailValidationButton.setOnClickListener {
             viewModel.getSendEmail(binding.emailEditTextView.text.toString())

            // 200일 때


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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sendEmailFlow.collectLatest { it ->
                    when(it?.code) {
                        SUCCESS -> {
                            binding.authCodeConstraint.visibility = View.VISIBLE
                            binding.emailValidationButton.isEnabled = false
                            startTimer()
                        }

                        E01_500 -> {} // 이메일 전송 실패

                        E02_400 -> {} // 유효하지 않은 이메일

                        null -> Toast.makeText(this@SignUpActivity, "서버에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.emailResendTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
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