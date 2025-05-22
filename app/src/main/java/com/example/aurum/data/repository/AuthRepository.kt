package com.example.aurum.data.repository

import com.example.aurum.data.remote.AuthApi
import com.example.aurum.data.remote.LoginRequest
import com.example.aurum.data.remote.LoginResponse
import com.example.aurum.data.remote.RegisterRequest
import com.example.aurum.data.remote.RegisterResponse

class AuthRepository(private val api: AuthApi) {
    suspend fun register(email: String, username: String, phone: String, password: String, dateOfBirth: String): RegisterResponse {
        return api.register(RegisterRequest(email, username, phone, password, dateOfBirth))
    }

    suspend fun login(loginOrEmail: String, password: String): LoginResponse {
        return api.login(LoginRequest(loginOrEmail, password))
    }
}
