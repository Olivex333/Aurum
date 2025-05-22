package com.example.aurum.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WishlistApiInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    // interceptor z logowaniem
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // klient OkHttp
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // właściwy Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    // i na koniec serwis
    val api: WishlistApi by lazy {
        retrofit.create(WishlistApi::class.java)
    }
}
