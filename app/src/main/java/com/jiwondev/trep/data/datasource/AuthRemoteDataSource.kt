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

lateinit var data: LoginResponse
class AuthRemoteDataSource(private val ioDispatcher: CoroutineDispatcher) {
    suspend fun login(userInfo: HashMap<String, String>): LoginResponse {
        withContext(ioDispatcher) {
            data = Retrofit.getInstance().create(AuthInterface::class.java).postLogin(userInfo).body()!!
        }
        return data
    }
}