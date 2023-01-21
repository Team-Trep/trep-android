package com.jiwondev.trep.network

import com.jiwondev.trep.model.dto.EmailCodeVerifyResponse
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.dto.SendEmailResponse
import com.jiwondev.trep.resource.Constant.Companion.POST_LOGIN
import com.jiwondev.trep.resource.Constant.Companion.POST_REFRESH_TOKEN
import com.jiwondev.trep.resource.Constant.Companion.POST_SIGN_UP
import com.jiwondev.trep.resource.Constant.Companion.TEST_VIDEO
import com.jiwondev.trep.resource.Constant.Companion.USER_EMAIL_SEND
import com.jiwondev.trep.resource.Constant.Companion.USER_EMAIL_VERIFY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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

    /** 이메일 전송 **/
    @GET(USER_EMAIL_SEND)
    suspend fun getSendEmail(
        @Query("email") email: String,
    ) : Response<SendEmailResponse>

    @GET(USER_EMAIL_VERIFY)
    suspend fun getCodeVerify(
        @Query("email") email: String,
        @Query("key") key: String
    ) : Response<EmailCodeVerifyResponse>

    @POST(TEST_VIDEO)
    suspend fun postTestVideo(
        @Header("Authorization") Authorization : String = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzI5Mjg4MzcsImV4cCI6MTY3MzAxNTIzNywic3ViIjoidGVzdDAxIn0.b5kdXnwbKT8eKTNYrCXclrbaBt6d8wXnj-pp_s_mi3I",
        @Header("Range") Range: String = "bytes=0-",
        @Body tokenInfo: HashMap<String, String>
    ) : ResponseBody
}