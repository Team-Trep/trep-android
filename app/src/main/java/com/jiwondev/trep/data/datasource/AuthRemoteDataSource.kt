package com.jiwondev.trep.data.datasource

import android.util.Log
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.dto.SendEmailResponse
import com.jiwondev.trep.network.AuthInterface
import com.jiwondev.trep.network.Retrofit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

const val TAG = "AuthRemoteDataSource : "

var data: LoginResponse? = null

class AuthRemoteDataSource(private val ioDispatcher: CoroutineDispatcher) {
    suspend fun login(userInfo: HashMap<String, String>): LoginResponse? {
        val data = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).postLogin(userInfo).body()
        }
        Log.d("data : ", data.toString())
        return data
    }

    suspend fun sendEmail(email: String): SendEmailResponse? {
        val response = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).getSendEmail(email).body()
        }
        return response
    }
//
//    suspend fun codeVeryfied(email: String, key: String) : {
//
//    }
}
