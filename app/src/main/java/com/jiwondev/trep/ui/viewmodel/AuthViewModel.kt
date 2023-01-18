package com.jiwondev.trep.ui.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.*
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.dto.SendEmailResponse
import com.jiwondev.trep.model.preference.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    var countDownTimer: CountDownTimer? = null
    var time = 180 // 인증 만료 시간 -> 3분

    val userPreferencesFlow: Flow<UserPreferences> = authRepository.userPreferencesFlow

    private var _loginFlow: MutableSharedFlow<LoginResponse?> = MutableSharedFlow()
    val loginFlow: SharedFlow<LoginResponse?>
        get() = _loginFlow.asSharedFlow()

    private var _sendEmailFlow: MutableSharedFlow<SendEmailResponse?> = MutableSharedFlow()
    val sendEmailFlow: SharedFlow<SendEmailResponse?>
        get() = _sendEmailFlow.asSharedFlow()

    // private var _codeVerified: MutableLiveData
//    val codeVerified: LiveData<>
//        get() = _codeVerified


    fun getLogin(userId: String, userPassword: String) = viewModelScope.launch {
        val userInfo = hashMapOf<String, String>()
        userInfo["username"] = userId
        userInfo["password"] = userPassword

        authRepository.getLoginInfo(userInfo).collectLatest {
            _loginFlow.emit(it)
        }
    }

    fun getSendEmail(email: String) = viewModelScope.launch {
        authRepository.getSendEmail(email).collectLatest {
            _sendEmailFlow.emit(it)
        }
    }

    fun getVerified(email: String, key: String) = viewModelScope.launch {
//        _codeVerified.value =
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
