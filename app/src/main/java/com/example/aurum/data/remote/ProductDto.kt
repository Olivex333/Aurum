package com.example.aurum.data.remote

data class ProductDto(
    val id: Int,
    val name: String,
    val price: Double,
    val image_url: String,
    val colors: String,
    val sizes: String,
    val description: String,
    val chosenColor: String? = null,
    val chosenSize: String? = null

)
