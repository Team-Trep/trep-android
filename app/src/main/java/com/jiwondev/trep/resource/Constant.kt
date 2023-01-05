package com.jiwondev.trep.resource

class Constant {
    companion object {
        const val TREP_PREFERENCE_NAME = "trep_preference" // datastore name

        /** api **/
        const val BASE_URL = "http://api-dev.net/"
        const val POST_SIGN_UP = "user/register" // 회원가입
        const val POST_LOGIN = "user/login" // 로그인
        const val POST_REFRESH_TOKEN = "user/refresh" // 토큰 재발급
    }
}