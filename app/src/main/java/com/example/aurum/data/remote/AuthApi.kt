package com.example.aurum.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(
    val email: String,
    val username: String,
    val phone: String,
    val password: String,
    val dateOfBirth: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String?
)

data class LoginRequest(
    val loginOrEmail: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val token: String?,
    val user: UserData?
)

data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val phone: String
)

data class ErrorResponse(
    val success: Boolean,
    val message: String?
)


interface AuthApi {
    @POST("register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponse

    @POST("login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}
