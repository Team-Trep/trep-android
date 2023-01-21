package com.jiwondev.trep.data.repository

import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import com.jiwondev.trep.data.datasource.AuthLocalDataSource
import com.jiwondev.trep.data.datasource.AuthRemoteDataSource
import com.jiwondev.trep.model.dto.EmailCodeVerifyResponse
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.dto.SendEmailResponse
import com.jiwondev.trep.model.preference.UserPreferences
import com.jiwondev.trep.network.AuthInterface
import com.jiwondev.trep.network.Retrofit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody
import java.io.IOException

class AuthRepository(private val authRemoteDataSource: AuthRemoteDataSource, private val authLocalDataSource: AuthLocalDataSource) {

    val userPreferencesFlow: Flow<UserPreferences> = authLocalDataSource.userPreferencesFlow

    suspend fun serUserInfo(accessToken: String, refreshToken: String) {
        authLocalDataSource.setUserInfo(accessToken, refreshToken)
    }

    suspend fun getLoginInfo(userInfo: HashMap<String, String>): Flow<LoginResponse?> = flow {
        /** TODO catch로 예외처리 **/
        emit(authRemoteDataSource.login(userInfo))
    }

    suspend fun getSendEmail(email: String): Flow<SendEmailResponse?> = flow {
        emit(authRemoteDataSource.sendEmail(email))
    }

    suspend fun getCodeVerify(email: String, key: String): Flow<EmailCodeVerifyResponse?> = flow {
        emit(authRemoteDataSource.codeVerify(email, key))
    }




    // Video Test
    suspend fun getByteVideo(): ResponseBody {
        Log.d("getByteVideo : ", "getByteVideo")
        val info = hashMapOf<String, String>()
        info["username"] = "test01"
        info["file"] = "352e8c5d-6382-4977-8daa-368c02643337.mp4"
        val data = Retrofit.getInstance().create(AuthInterface::class.java).postTestVideo(tokenInfo = info)
        Log.d("testData : ", data.toString())
        return data
    }
}