package com.jiwondev.trep.data.repository

import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.model.LoginResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource) {
    suspend fun getLoginInfo(userInfo: HashMap<String, String>) : Flow<LoginResponse> = flow {
        authRemoteDataSource.login(userInfo)
    }
}