package com.example.aurum.data.remote

import retrofit2.http.GET

interface AurumApi {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>

}



