package com.jiwondev.trep.data.repository

import android.util.Log
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.model.LoginResponse
import com.jiwondev.trep.network.AuthInterface
import com.jiwondev.trep.network.Retrofit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

class AuthRepository(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) {
    suspend fun getLoginInfo(userInfo: HashMap<String, String>): Flow<LoginResponse?> = flow {
        Log.d("getLoginInfo() : ", "진입")
        emit(authRemoteDataSource.login(userInfo))
    }
}