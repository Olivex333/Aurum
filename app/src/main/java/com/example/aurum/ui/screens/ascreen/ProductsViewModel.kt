package com.example.aurum.ui.screens.ascreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aurum.data.remote.ProductDto
import com.example.aurum.data.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val repository: ProductsRepository = ProductsRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> get() = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = repository.fetchProducts()
                _products.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
