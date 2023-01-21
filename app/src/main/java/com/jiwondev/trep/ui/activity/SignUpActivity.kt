package com.jiwondev.trep.ui.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
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
import com.jiwondev.trep.resource.Utils
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
        /** 이메일 전송 **/
        binding.emailValidationButton.setOnClickListener {
            if(binding.emailEditTextView.text.toString().isNotEmpty()) {
                binding.progressConst.visibility = View.VISIBLE
                Toast.makeText(this, "인증코드를 전송중입니다. 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
                viewModel.email = binding.emailEditTextView.text.toString()
                viewModel.getSendEmail(binding.emailEditTextView.text.toString())
            } else {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        /** 인증코드 인증 **/
        binding.codeValidationButton.setOnClickListener {
            if(binding.codeEditTextView.text.toString().isNotEmpty()) {
                viewModel.getVerified(email = viewModel.email, key = binding.codeEditTextView.text.toString())
            } else {
                Toast.makeText(this, "인증코드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        /** 이메일 EditText **/
        binding.emailEditTextView.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(Utils.checkEmailRegex(inputResult.toString())) {
                    true -> updateUi(binding.signUpEmailFormatErrorTextView, binding.emailEditTextView, true)
                    false -> updateUi(binding.signUpEmailFormatErrorTextView, binding.emailEditTextView, false)
                }
            }
        })

        /** 패스워드 EditText **/
        binding.signUpPasswordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(Utils.checkPasswordRegex(inputResult.toString())) {
                    true -> updateUi(binding.passwordErrorTextView, binding.signUpPasswordEditText, true)
                    false -> updateUi(binding.passwordErrorTextView, binding.signUpPasswordEditText, false)
                }
            }
        })

        /** 패스워드 확인 EditText **/
        binding.signUpPasswordCheckEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(inputResult.toString() == binding.signUpPasswordEditText.text.toString()) {
                    true -> updateUi(binding.passwordCheckErrorTextView, binding.signUpPasswordCheckEditText, true)
                    false -> updateUi(binding.passwordCheckErrorTextView, binding.signUpPasswordCheckEditText, false)
                }
            }
        })



        binding.backButton.setOnClickListener {

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
                /** 에미일 인증 **/
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

                /** 인증코드 인증 **/
                viewModel.codeVerified.collectLatest {
                    when(it?.code) {
                        SUCCESS -> {
                            if(it.verified) {
                                Toast.makeText(this@SignUpActivity, "인증되었습니다.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@SignUpActivity, "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        E03_400 -> Toast.makeText(this@SignUpActivity, "인증번호가 만료되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        E06_400 -> Toast.makeText(this@SignUpActivity, "이미 인증된 이메일입니다.", Toast.LENGTH_SHORT).show()
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

    private fun updateUi(textView: TextView, editText: EditText, visible: Boolean) {
        /** AppCompatResources : Resource에 엑세스 하기 위해서 사용. **/
        if(visible) {
            textView.visibility = View.GONE
            editText.background = AppCompatResources.getDrawable(this@SignUpActivity, R.drawable.edit_bg_grey_radius_10dp)

        } else {
            textView.visibility = View.VISIBLE
            editText.background = AppCompatResources.getDrawable(this@SignUpActivity, R.drawable.edit_error_bg_grey_radius_10dp)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.countDownTimer?.cancel()
    }
}