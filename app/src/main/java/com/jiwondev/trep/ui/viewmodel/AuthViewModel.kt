package com.jiwondev.trep.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.model.dto.LoginResponse
import com.jiwondev.trep.model.preference.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "AuthViewModel"

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val userPreferencesFlow: Flow<UserPreferences> = authRepository.userPreferencesFlow

    private val _loginFlow: MutableStateFlow<LoginResponse?> = MutableStateFlow(LoginResponse())
    val loginFlow: StateFlow<LoginResponse?>
        get() = _loginFlow.asStateFlow()

    fun getLogin() = viewModelScope.launch {
        Log.d(TAG, "getLogin")
        // TODO : UI 구현 후 삭제

        val userInfo = hashMapOf<String, String>()
        userInfo["username"] = "test01"
        userInfo["password"] = "test012"

        authRepository.getLoginInfo(userInfo).collect {
            Log.d("getLoginInfo : ", it.toString())
            _loginFlow.value = it
        }
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
