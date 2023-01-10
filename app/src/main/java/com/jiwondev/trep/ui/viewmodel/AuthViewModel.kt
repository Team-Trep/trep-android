package com.jiwondev.trep.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.preference.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val userPreferencesFlow: Flow<UserPreferences> = authRepository.userPreferencesFlow

    private val _loginFlow: MutableSharedFlow<LoginResponse?> = MutableSharedFlow()
    val loginFlow: SharedFlow<LoginResponse?>
        get() = _loginFlow.asSharedFlow()

    fun getLogin(userId: String, userPassword: String) = viewModelScope.launch {
        val userInfo = hashMapOf<String, String>()
        userInfo["username"] = userId
        userInfo["password"] = userPassword

        authRepository.getLoginInfo(userInfo).collectLatest {
            _loginFlow.emit(it)
        }
    }

    /** Datastore write **/
    fun setUserInfo(accessToken: String, refreshToken: String) = viewModelScope.launch {
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
