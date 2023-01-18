package com.jiwondev.trep.resource

import com.google.gson.JsonObject
import com.jiwondev.trep.model.dto.ExceptionResponse
import com.jiwondev.trep.model.dto.LoginResponse
import okhttp3.ResponseBody
import org.json.JSONObject
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

        fun parseErrorBodyCode(errorBody: ResponseBody?): String? {
            val paresResult = errorBody?.let {
                JSONObject(errorBody.string()).getString("code")
            }
            return paresResult
        }
    }
}