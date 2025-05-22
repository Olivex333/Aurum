package com.example.aurum.data.repository

import com.example.aurum.data.remote.AddWishlistRequest
import com.example.aurum.data.remote.WishlistApi
import com.example.aurum.data.remote.WishlistApiInstance
import com.example.aurum.data.remote.WishlistItemDto

class WishlistRepository(
    private val api: WishlistApi = WishlistApiInstance.api
) {
    suspend fun getWishlist(token: String): List<WishlistItemDto> {
        val response = api.getWishlist(token)
        if (response.success) {
            return response.items
        } else {
            throw Exception("Błąd pobierania wishlisty")
        }
    }

    suspend fun addWishlist(token: String, productId: Int) {
        val body = AddWishlistRequest(productId)
        val response = api.addWishlist(token, body)
        if (!response.success) {
            throw Exception(response.message ?: "Błąd dodawania do wishlist")
        }
    }

    suspend fun removeWishlist(token: String, wishlistItemId: Int) {
        val response = api.removeWishlist(token, wishlistItemId)
        if (!response.success) {
            throw Exception(response.message ?: "Błąd usuwania z wishlist")
        }
    }
}
