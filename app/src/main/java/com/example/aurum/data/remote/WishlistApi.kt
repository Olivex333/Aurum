package com.example.aurum.data.remote

import retrofit2.http.*

data class WishlistItemDto(
    val wishlist_item_id: Int,
    val date_added: String?,
    val product_id: Int,
    val name: String,
    val price: Double,
    val image_url: String,
    val colors: String,
    val sizes: String,
    val description: String
)

data class GetWishlistResponse(
    val success: Boolean,
    val items: List<WishlistItemDto>
)

data class AddWishlistRequest(val productId: Int)
data class AddWishlistResponse(val success: Boolean, val message: String?)
data class RemoveWishlistResponse(val success: Boolean, val message: String?)

interface WishlistApi {
    @GET("wishlist")
    suspend fun getWishlist(
        @Header("Authorization") token: String
    ): GetWishlistResponse

    @POST("wishlist")
    suspend fun addWishlist(
        @Header("Authorization") token: String,
        @Body body: AddWishlistRequest
    ): AddWishlistResponse

    @DELETE("wishlist/{wishlistItemId}")
    suspend fun removeWishlist(
        @Header("Authorization") token: String,
        @Path("wishlistItemId") wishlistItemId: Int
    ): RemoveWishlistResponse
}
