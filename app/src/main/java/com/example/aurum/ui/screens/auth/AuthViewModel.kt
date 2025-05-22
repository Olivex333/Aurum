package com.example.aurum.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aurum.data.remote.AuthApiInstance
import com.example.aurum.data.remote.ErrorResponse
import com.example.aurum.data.remote.UserData
import com.example.aurum.data.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException // <-- to jest ważne

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository(AuthApiInstance.api)

    private val _signUpState = MutableStateFlow<SignUpUiState>(SignUpUiState.Idle)
    val signUpState: StateFlow<SignUpUiState> = _signUpState

    private val _loginState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val loginState: StateFlow<LoginUiState> = _loginState

    var currentUser: UserData? = null
    var token: String? = null

    fun register(email: String, username: String, phone: String, password: String, dateOfBirth: String) {
        viewModelScope.launch {
            try {
                _signUpState.value = SignUpUiState.Loading
                val result = repository.register(email, username, phone, password, dateOfBirth)
                if (result.success) {
                    _signUpState.value = SignUpUiState.Success(result.message ?: "")
                } else {
                    _signUpState.value = SignUpUiState.Error(result.message ?: "")
                }
            } catch (e: Exception) {
                _signUpState.value = SignUpUiState.Error(e.localizedMessage ?: "")
            }
        }
    }

    fun login(loginOrEmail: String, password: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginUiState.Loading
                val result = repository.login(loginOrEmail, password) // <--- repository, nie authRepository
                if (result.success && result.user != null) {
                    token = result.token
                    currentUser = result.user
                    _loginState.value = LoginUiState.Success(token ?: "", currentUser!!)
                } else {
                    _loginState.value = LoginUiState.Error(result.message ?: "Błąd logowania")
                }
            } catch (e: HttpException) {
                val parsedMsg = parseError(e)
                _loginState.value = LoginUiState.Error(parsedMsg)
            } catch (e: Exception) {
                _loginState.value = LoginUiState.Error(e.localizedMessage ?: "Exception")
            }
        }
    }

    private fun parseError(e: HttpException): String {
        return try {
            val body = e.response()?.errorBody()?.string()
            if (!body.isNullOrBlank()) {
                val gson = Gson()
                val errorResp = gson.fromJson(body, ErrorResponse::class.java)
                errorResp.message ?: "Błąd serwera"
            } else {
                "Błąd serwera (brak informacji)"
            }
        } catch (ex: Exception) {
            "Błąd serwera (nie można sparsować)"
        }
    }
}

sealed class SignUpUiState {
    object Idle : SignUpUiState()
    object Loading : SignUpUiState()
    data class Success(val message: String) : SignUpUiState()
    data class Error(val error: String) : SignUpUiState()
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val token: String, val user: UserData) : LoginUiState()
    data class Error(val error: String) : LoginUiState()
}
