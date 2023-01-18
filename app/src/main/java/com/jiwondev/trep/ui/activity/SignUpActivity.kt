package com.jiwondev.trep.ui.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
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
import com.jiwondev.trep.resource.Constant.Companion.E06_400
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
            binding.progressConst.visibility = View.VISIBLE
            Toast.makeText(this, "인증코드를 전송중입니다. 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
            viewModel.getSendEmail(binding.emailEditTextView.text.toString())
        }

        binding.backButton.setOnClickListener {

        }

        binding.codeValidationButton.setOnClickListener {

        }

        binding.emailResendTextView.setOnClickListener {

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
                            Toast.makeText(this@SignUpActivity, "인증번호가 발송되었습니다.", Toast.LENGTH_SHORT).show()
                            binding.progressConst.visibility = View.GONE
                            binding.authCodeConstraint.visibility = View.VISIBLE
                            binding.emailValidationButton.isEnabled = false
                            startTimer()
                        }

                        /** 이메일 전송 실패 **/
                        E01_500, null -> {
                            Toast.makeText(this@SignUpActivity, "이메일 전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            binding.progressConst.visibility = View.GONE
                        }

                        /** 유효하지 않은 이메일**/
                        E02_400 -> {
                            Toast.makeText(this@SignUpActivity, "유효하지 않은 이메일입니다.", Toast.LENGTH_SHORT).show()
                            binding.progressConst.visibility = View.GONE
                        }

                        /** 이메일이 전소된지 1분 미만 **/
                        E06_400 -> {
                            Toast.makeText(this@SignUpActivity, "잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            binding.progressConst.visibility = View.GONE
                        }
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