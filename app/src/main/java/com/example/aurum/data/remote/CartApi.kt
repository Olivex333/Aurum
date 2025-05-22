package com.example.aurum.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class AddToCartRequest(
    val productId: Int,
    val quantity: Int = 1,
    val chosenColor: String? = null,
    val chosenSize: String? = null
)


data class AddToCartResponse(
    val success: Boolean,
    val message: String?
)

data class CartItemDto(
    val cart_item_id: Int,
    val quantity: Int,
    val product_id: Int,
    val name: String,
    val price: Double,
    val image_url: String,
    val colors: String,
    val sizes: String,
    val description: String,
    val chosen_color: String? = null,
    val chosen_size: String? = null
)

data class GetCartResponse(
    val success: Boolean,
    val cartItems: List<CartItemDto>
)

data class RemoveItemResponse(
    val success: Boolean,
    val message: String?
)

interface CartApi {

    @GET("cart")
    suspend fun getCart(
        @Header("Authorization") token: String
    ): GetCartResponse

    @POST("cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body body: AddToCartRequest
    ): AddToCartResponse

    @DELETE("cart/{cartItemId}")
    suspend fun removeFromCart(
        @Header("Authorization") token: String,
        @Path("cartItemId") cartItemId: Int
    ): RemoveItemResponse
}
