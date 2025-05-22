package com.example.aurum.data.repository

import com.example.aurum.data.remote.ProductDto
import com.example.aurum.data.remote.RetrofitClient

class ProductsRepository {
    suspend fun fetchProducts(): List<ProductDto> {
        return RetrofitClient.aurumApi.getProducts()

    }
}
