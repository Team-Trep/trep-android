package com.jiwondev.trep.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.model.dto.EmailCodeVerifyResponse
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.dto.SendEmailResponse
import com.jiwondev.trep.model.preference.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var countDownTimer: CountDownTimer? = null
    var email: String = "" // 이메일
    var time = 180 // 인증 만료 시간 -> 3분

    val userPreferencesFlow: Flow<UserPreferences> = authRepository.userPreferencesFlow

    /** 로그인 **/
    private var _loginFlow: MutableSharedFlow<LoginResponse?> = MutableSharedFlow()
    val loginFlow: SharedFlow<LoginResponse?>
        get() = _loginFlow.asSharedFlow()

    /** 이메일 전송 **/
    private var _sendEmailFlow: MutableSharedFlow<SendEmailResponse?> = MutableSharedFlow()
    val sendEmailFlow: SharedFlow<SendEmailResponse?>
        get() = _sendEmailFlow.asSharedFlow()

    /** 인증코드 확인 **/
     private var _codeVerified: MutableSharedFlow<EmailCodeVerifyResponse?> = MutableSharedFlow()
     val codeVerified: SharedFlow<EmailCodeVerifyResponse?>
        get() = _codeVerified.asSharedFlow()


    /** 로그인 **/
    fun getLogin(userId: String, userPassword: String) = viewModelScope.launch {
        val userInfo = hashMapOf<String, String>()
        userInfo["username"] = userId
        userInfo["password"] = userPassword

        authRepository.getLoginInfo(userInfo).collectLatest {
            _loginFlow.emit(it)
        }
    }

    /** 이메일 전송 **/
    fun getSendEmail(email: String) = viewModelScope.launch {
        authRepository.getSendEmail(email).collectLatest {
            _sendEmailFlow.emit(it)
        }
    }

    /** 인증코드 인증 **/
    fun getVerified(email: String, key: String) = viewModelScope.launch {
        authRepository.getCodeVerify(email, key).collectLatest {
            _codeVerified.emit(it)
        }
    }

    /** Datastore write **/
    suspend fun setUserInfo(accessToken: String, refreshToken: String) = viewModelScope.launch {
        authRepository.serUserInfo(accessToken, refreshToken)
    }
}


class AuthViewModelFactory(private val param: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            AuthViewModel(param) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
