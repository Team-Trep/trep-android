package com.jiwondev.trep.data.datasource

import android.util.Log
import com.jiwondev.trep.model.dto.LoginResponse
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
        withContext(ioDispatcher) {
            val response = Retrofit.getInstance().create(AuthInterface::class.java).postLogin(userInfo)
            when(response.isSuccessful) {
                true -> {
                    data = response.body()
                    data?.code = response.code()
                }
                false -> data = LoginResponse(code = response.code())
            }
        }
        return data
    }

    suspend fun sendEmail(email: String): Int {
        val code = withContext(ioDispatcher) {
            Retrofit.getInstance().create(AuthInterface::class.java).getSendEmail(email).code()
        }
        return code
    }
//
//    suspend fun codeVeryfied(email: String, key: String) : {
//
//    }
}
