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
import com.jiwondev.trep.resource.Constant.Companion.E04_400
import com.jiwondev.trep.resource.Constant.Companion.E05_400
import com.jiwondev.trep.resource.Constant.Companion.E06_400
import com.jiwondev.trep.resource.Constant.Companion.SUCCESS
import com.jiwondev.trep.resource.Constant.Companion.U05_400
import com.jiwondev.trep.resource.Constant.Companion.U06_400
import com.jiwondev.trep.resource.Constant.Companion.U07_400
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
        collectFlows()
        listener()
    }

    private fun listener() {
        /** ????????? ?????? **/
        binding.emailValidationButton.setOnClickListener {
            if(binding.emailEditTextView.text.toString().isNotEmpty()) {
                updateProgressMessageUi("??????????????? ??????????????????. ????????? ??????????????????")
                viewModel.email = binding.emailEditTextView.text.toString()
                viewModel.getSendEmail(binding.emailEditTextView.text.toString())
            } else {
                Toast.makeText(this, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            }
        }

        /** ???????????? ?????? **/
        binding.codeValidationButton.setOnClickListener {
            if(binding.codeEditTextView.text.toString().isNotEmpty()) {
                viewModel.getVerified(email = viewModel.email, key = binding.codeEditTextView.text.toString())
            } else {
                Toast.makeText(this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            }
        }

        /** ????????? EditText **/
        binding.emailEditTextView.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(Utils.checkEmailRegex(inputResult.toString())) {
                    true -> updateEditTextUi(binding.signUpEmailFormatErrorTextView, binding.emailEditTextView, true)
                    false -> updateEditTextUi(binding.signUpEmailFormatErrorTextView, binding.emailEditTextView, false)
                }
            }
        })

        /** ???????????? EditText **/
        binding.signUpPasswordEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(Utils.checkPasswordRegex(inputResult.toString())) {
                    true -> updateEditTextUi(binding.passwordErrorTextView, binding.signUpPasswordEditText, true)
                    false -> updateEditTextUi(binding.passwordErrorTextView, binding.signUpPasswordEditText, false)
                }
            }
        })

        /** ???????????? ?????? EditText **/
        binding.signUpPasswordCheckEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(inputResult: Editable?) {
                when(inputResult.toString() == binding.signUpPasswordEditText.text.toString()) {
                    true -> updateEditTextUi(binding.passwordCheckErrorTextView, binding.signUpPasswordCheckEditText, true)
                    false -> updateEditTextUi(binding.passwordCheckErrorTextView, binding.signUpPasswordCheckEditText, false)
                }
            }
        })

        binding.backButton.setOnClickListener {

        }

        /** ????????? ????????? **/
        binding.emailResendTextView.setOnClickListener {
            updateProgressMessageUi("??????????????? ??????????????????. ????????? ??????????????????")
            viewModel.email = binding.emailEditTextView.text.toString()
            viewModel.getSendEmail(binding.emailEditTextView.text.toString())
        }

        binding.signUpTextView.setOnClickListener {
            viewModel.signUp(
                viewModel.email,
                binding.signUpPasswordEditText.text.toString().trim(),
                binding.signUpNicknameEditTextView.text.toString().trim()
            )
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

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                /** ????????? ?????? **/
                launch {
                    viewModel.sendEmailFlow.collectLatest { it ->
                        when(it?.code) {
                            SUCCESS -> {
                                updateProgressMessageUi("??????????????? ?????????????????????.")
                                binding.authCodeConstraint.visibility = View.VISIBLE
                                binding.emailValidationButton.isEnabled = false
                                startTimer()
                            }

                            E01_500, null -> updateProgressMessageUi("????????? ????????? ??????????????????.")
                            E02_400 -> updateProgressMessageUi("???????????? ?????? ??????????????????.")
                            E06_400 -> updateProgressMessageUi("???????????? ???????????? 1??? ???????????????. ????????? ?????? ??????????????????.")
                            E05_400 -> updateProgressMessageUi("?????? ????????? ????????? ?????????.")
                        }
                    }
                }

                launch {
                    /** ???????????? ?????? **/
                    viewModel.codeVerifiedFlow.collectLatest {
                        when(it?.code) {
                            SUCCESS -> {
                                if(it.verified) {
                                    updateProgressMessageUi("?????????????????????.")
                                    viewModel.countDownTimer?.onFinish()
                                } else {
                                    Toast.makeText(this@SignUpActivity, "??????????????? ????????????.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            E03_400 -> Toast.makeText(this@SignUpActivity, "??????????????? ?????????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
                            E06_400 -> Toast.makeText(this@SignUpActivity, "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                            null -> Toast.makeText(this@SignUpActivity, "????????????", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(this@SignUpActivity, "test : ${it.toString()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                launch {
                    /** ???????????? **/
                    viewModel.signUpFlow.collectLatest {
                        when(it?.code) {
                            SUCCESS -> {
                                Toast.makeText(this@SignUpActivity, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                                finish()
                            }

                            /** ????????? ?????? **/
                            U07_400 -> {
                                Toast.makeText(this@SignUpActivity, "????????? ????????? ?????????.", Toast.LENGTH_SHORT).show()
                                binding.nicknameErrorTextView.visibility = View.VISIBLE
                                binding.signUpNicknameEditTextView.background = AppCompatResources.getDrawable(this@SignUpActivity, R.drawable.edit_error_bg_grey_radius_10dp)
                            }

                            U05_400 -> Toast.makeText(this@SignUpActivity, "????????? ??????????????????.", Toast.LENGTH_SHORT).show() // ???????????? ????????? ??????
                            U06_400 -> Toast.makeText(this@SignUpActivity, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                            E04_400 -> Toast.makeText(this@SignUpActivity, "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun startTimer() {
        viewModel.countDownTimer?.cancel()
        var time = 180
        viewModel.countDownTimer = object : CountDownTimer(180000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                time--
                binding.minuteTextView.text = "0${time/60}:"
                when {
                    time%60 >= 10 -> binding.secondTextView.text = "${time%60}"
                    else -> binding.secondTextView.text = "0${time%60}"
                }
            }

            override fun onFinish() {
                viewModel.countDownTimer?.cancel()
            }
        }
        viewModel.countDownTimer!!.start()
    }

    private fun updateEditTextUi(textView: TextView, editText: EditText, visible: Boolean) {
        /** AppCompatResources : Resource??? ????????? ?????? ????????? ??????. **/
        if(visible) {
            textView.visibility = View.GONE
            editText.background = AppCompatResources.getDrawable(this@SignUpActivity, R.drawable.edit_bg_grey_radius_10dp)

        } else {
            textView.visibility = View.VISIBLE
            editText.background = AppCompatResources.getDrawable(this@SignUpActivity, R.drawable.edit_error_bg_grey_radius_10dp)
        }
    }

    private fun updateProgressMessageUi(message: String) {
        Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
        binding.progressConst.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.countDownTimer?.cancel()
    }
}