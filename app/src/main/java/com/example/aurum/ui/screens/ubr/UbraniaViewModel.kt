package com.example.aurum.ui.screens.ubr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aurum.data.remote.ProductDto
import com.example.aurum.data.repository.UbraniaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UbraniaViewModel(
    private val repository: UbraniaRepository = UbraniaRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products: StateFlow<List<ProductDto>> get() = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun loadUbrania() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val result = repository.fetchClothingProducts()
                _products.value = result
            } catch (e: Exception) {
                _error.value = "Błąd: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
