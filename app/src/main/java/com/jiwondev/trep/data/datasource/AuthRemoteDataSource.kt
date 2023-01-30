package com.jiwondev.trep.data.datasource

import android.util.Log
import com.bumptech.glide.util.Util
import com.jiwondev.trep.model.dto.EmailCodeVerifyResponse
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.dto.SendEmailResponse
import com.jiwondev.trep.model.dto.SignUpResponse
import com.jiwondev.trep.network.AuthInterface
import com.jiwondev.trep.network.Retrofit
import com.jiwondev.trep.resource.Utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

const val TAG = "AuthRemoteDataSource : "

var data: LoginResponse? = null

class AuthRemoteDataSource(private val ioDispatcher: CoroutineDispatcher) {
    suspend fun login(userInfo: HashMap<String, String>): LoginResponse? {
        val response = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).postLogin(userInfo)
        }
        return if(response.isSuccessful) {
            response.body()
        } else  {
            LoginResponse(code = Utils.parseErrorBodyCode(response.errorBody()))
        }
    }

    suspend fun sendEmail(email: String): SendEmailResponse? {
        val response = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).getSendEmail(email)
        }
        return if(response.isSuccessful) {
            response.body()
        } else  {
            SendEmailResponse(code = Utils.parseErrorBodyCode(response.errorBody()))
        }
    }

    suspend fun codeVerify(email: String, key: String): EmailCodeVerifyResponse {
        val response = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).getCodeVerify(email, key)
        }
        Log.d("codeResponse : ", "res : ${response.body().toString()}")
        return if(response.isSuccessful) {
            Log.d("isSuccess : ", "true")
            EmailCodeVerifyResponse(verified = response.body()?.verified ?: false)
        } else  {
            Log.d("isSuccess : ", "false")
            EmailCodeVerifyResponse(code = Utils.parseErrorBodyCode(response.errorBody()))
        }
    }

    suspend fun signUp(body: HashMap<String, String>): SignUpResponse? {
        val response = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).postSignUp(body)
        }
        return if(response.isSuccessful) {
            response.body()?.let { SignUpResponse(id = it.id)}
        } else  {
            SignUpResponse(code = Utils.parseErrorBodyCode(response.errorBody()))
        }
    }
}
