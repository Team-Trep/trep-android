package com.jiwondev.trep.network

import com.jiwondev.trep.model.LoginResponse
import com.jiwondev.trep.resource.Constant.Companion.POST_LOGIN
import com.jiwondev.trep.resource.Constant.Companion.POST_REFRESH_TOKEN
import com.jiwondev.trep.resource.Constant.Companion.POST_SIGN_UP
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthInterface {
    /** 회원가입 **/
    @POST(POST_SIGN_UP)
    suspend fun postSignUp(
        @Body userInfo: HashMap<String, String>
    ) : Response<String>

    /** 로그인 **/
    @POST(POST_LOGIN)
    suspend fun postLogin(
        @Body userInfo: HashMap<String, String>
    ) : Response<LoginResponse>

    /** 토큰 재발급 **/
    @POST(POST_REFRESH_TOKEN)
    suspend fun postTokenRefresh(
        @Header("Authorization") Authorization : String = "", // ${App.prefs.getStringData(Constant.access_token)}
        @Header("username") username: String = "", // ${App.prefs.getStringData(Constant.username)}
        @Body tokenInfo: HashMap<String, String>
    ) : Response<LoginResponse>
}