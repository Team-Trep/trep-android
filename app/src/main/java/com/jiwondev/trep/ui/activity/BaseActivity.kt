package com.jiwondev.trep.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jiwondev.trep.R

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}