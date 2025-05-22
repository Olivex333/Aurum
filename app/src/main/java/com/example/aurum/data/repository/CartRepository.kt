package com.example.aurum.data.repository

import com.example.aurum.data.remote.AddToCartRequest
import com.example.aurum.data.remote.CartApi
import com.example.aurum.data.remote.CartApiInstance
import com.example.aurum.data.remote.CartItemDto

class CartRepository(
    private val api: CartApi = CartApiInstance.api
) {
    suspend fun getCart(token: String): List<CartItemDto> {
        // token to "Bearer xyz"
        val response = api.getCart(token)
        if (response.success) {
            return response.cartItems
        } else {
            throw Exception("Błąd pobierania koszyka")
        }
    }

    suspend fun addToCart(token: String, productId: Int, quantity: Int = 1) {
        val body = AddToCartRequest(productId, quantity)
        val response = api.addToCart(token, body)
        if (!response.success) {
            throw Exception(response.message ?: "Błąd dodawania do koszyka")
        }
    }

    suspend fun removeFromCart(token: String, cartItemId: Int) {
        val response = api.removeFromCart(token, cartItemId)
        if (!response.success) {
            throw Exception(response.message ?: "Błąd usuwania z koszyka")
        }
    }
}
