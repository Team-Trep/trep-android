package com.jiwondev.trep.resource

import java.util.regex.Pattern

class Utils {
    companion object {
        fun checkEmailRegex(email: String): Boolean {
            val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            return Pattern.matches(emailValidation, email)
        }

        fun checkPasswordRegex(password: String): Boolean {
            val passwordValidation = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#\$%^&*()+|=])[A-Za-z\\d~!@#\$%^&*()+|=]{8,20}\$"
            return Pattern.matches(passwordValidation, password)
        }
    }
}