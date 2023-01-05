package com.jiwondev.trep.ui.viewmodel

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jiwondev.trep.data.repository.AuthRepository
import com.jiwondev.trep.model.LoginResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

private const val TAG = "AuthViewModel"

data class AuthPreferences(
    val auto_login: Boolean
)

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<AuthPreferences>
) : ViewModel() {
    private val _loginFlow: MutableStateFlow<LoginResponse?> = MutableStateFlow(LoginResponse())
    val loginFlow: StateFlow<LoginResponse?>
        get() = _loginFlow.asStateFlow()

    fun getLogin() = viewModelScope.launch {
        Log.d(TAG, "getLogin")
        // TODO : UI 구현 후 삭제
        val userInfo = hashMapOf<String, String>()
        userInfo["username"] = "test01"
        userInfo["password"] = "test01"

        authRepository.getLoginInfo(userInfo).collect {
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
