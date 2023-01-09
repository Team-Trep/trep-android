package com.jiwondev.trep.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiwondev.trep.R
import com.jiwondev.trep.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<ActivitySignUpBinding>({ActivitySignUpBinding.inflate(it)}) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }
}