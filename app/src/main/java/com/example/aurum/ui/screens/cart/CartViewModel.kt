package com.example.aurum.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aurum.data.remote.CartItemDto
import com.example.aurum.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CartUiState {
    object Idle : CartUiState()
    object Loading : CartUiState()
    data class Success(val items: List<CartItemDto>) : CartUiState()
    data class Error(val message: String) : CartUiState()
}

class CartViewModel(
    private val repository: CartRepository = CartRepository()
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartUiState>(CartUiState.Idle)
    val cartState: StateFlow<CartUiState> get() = _cartState

    fun loadCart(token: String) {
        viewModelScope.launch {
            try {
                _cartState.value = CartUiState.Loading
                val cartItems = repository.getCart(token)
                _cartState.value = CartUiState.Success(cartItems)
            } catch (e: Exception) {
                _cartState.value = CartUiState.Error(e.message ?: "Błąd koszyka")
            }
        }
    }

    fun addItem(token: String, productId: Int, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                // Najpierw dodaj
                repository.addToCart(token, productId, quantity)
                // Potem przeładuj koszyk
                loadCart(token)
            } catch (e: Exception) {
                _cartState.value = CartUiState.Error(e.message ?: "Błąd dodawania do koszyka")
            }
        }
    }

    fun removeItem(token: String, cartItemId: Int) {
        viewModelScope.launch {
            try {
                repository.removeFromCart(token, cartItemId)
                loadCart(token)
            } catch (e: Exception) {
                _cartState.value = CartUiState.Error(e.message ?: "Błąd usuwania z koszyka")
            }
        }
    }
}
