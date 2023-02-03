package com.jiwondev.trep.ui.activity

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.databinding.ActivityIntroBinding
import com.jiwondev.trep.resource.App.Companion.coroutineDispatcher
import com.jiwondev.trep.resource.App.Companion.dataStore
import com.jiwondev.trep.resource.Constant.Companion.SUCCESS
import com.jiwondev.trep.resource.Constant.Companion.U01_404
import com.jiwondev.trep.resource.Constant.Companion.U02_400
import com.jiwondev.trep.resource.Utils
import com.jiwondev.trep.ui.viewmodel.AuthViewModel
import com.jiwondev.trep.ui.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class IntroActivity : BaseActivity<ActivityIntroBinding>({ ActivityIntroBinding.inflate(it)}) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        clickListener()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

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

        binding.googleLoginButton.setOnClickListener {
            // TODO : ActivityResultContrant로 Migration
            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(signInIntent, 1001)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Log.d("account : ", account.toString())
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("ApiException", "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
}
