package com.jiwondev.trep.resource

class Constant {
    companion object {
        const val TREP_PREFERENCE_NAME = "trep_preference" // datastore name

        /** api **/
        const val BASE_URL = "http://api-dev.net/"
        const val POST_SIGN_UP = "user/register" // 회원가입
        const val POST_LOGIN = "user/login" // 로그인
        const val POST_REFRESH_TOKEN = "user/refresh" // 토큰 재발급
        const val USER_EMAIL_SEND = "email/send" // 이메일 인증코드 전송
        const val USER_EMAIL_VERIFY = "email/verify" // 이메일 인증코드 확인
        const val TEST_VIDEO = "video/streaming" // 테스트 비디오


        /** error code **/
        const val SUCCESS = "0"

        const val U01_404 = "U01" // 회원정보 없음
        const val U02_400 = "U02" // 패스워드 불일치
        const val U03_400 = "U03" // 유효하지 않은 토큰
        const val U04_400 = "U04" // 토큰, 리프레시 토큰이 서로 일치하지 않음
        const val U05_404 = "U05" // 회원가입 데이터가 비어있음
        const val U06_400 = "U06" // 이미 존재하는 회원
        const val U07_400 = "U07" // 이미 존재하는 닉네임
        const val U08_400 = "U08" // 토큰 기한 만료
        const val U09_400 = "U09" // Authorization 헤더가 비어있음

        const val E01_500 = "E01" // 이메일 전송 실패
        const val E02_400 = "E02" // 유효하지 않은 이메일
        const val E03_400 = "E03" // 이메일 인증번호 기한 만료
        const val E04_400 = "E04" // 이메일이 인증되지 않았음
        const val E05_400 = "E05" // 이미 인증된 이메일
        const val E06_400 = "E06" // 인증메일이 전송된지 1분 미만
    }
}