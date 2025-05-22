package com.example.aurum.data.repository

import com.example.aurum.data.remote.ProductDto
import com.example.aurum.data.remote.RetrofitClient

class UbraniaRepository {
    suspend fun fetchClothingProducts(): List<ProductDto> {
        return RetrofitClient.aurumApi.getProducts()
    }
}
