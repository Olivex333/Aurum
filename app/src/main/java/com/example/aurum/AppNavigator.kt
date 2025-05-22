package com.example.aurum

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aurum.data.remote.ProductDto
import com.example.aurum.ui.screens.auth.AuthScreen
import com.example.aurum.ui.screens.auth.AuthViewModel
import com.example.aurum.ui.screens.cart.CartViewModel
import com.example.aurum.ui.screens.loading.LoadingScreen
import com.example.aurum.ui.screens.main.MainScreen
import com.example.aurum.ui.screens.productdetail.ProductDetailScreen

sealed class AppScreenState {
    object Loading : AppScreenState()
    object Main : AppScreenState()

    // Dodajemy nowy stan: szczegóły produktu
    data class Detail(val product: ProductDto) : AppScreenState()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator() {
    var screenState by remember { mutableStateOf<AppScreenState>(AppScreenState.Loading) }
    var showLogin by remember { mutableStateOf(false) }

    // ViewModel-e
    val authVm: AuthViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    // Jeśli nie potrzebujesz lokalnej listy cartItems, możesz to usunąć.
    val cartItems = remember { mutableStateListOf<ProductDto>() }

    AnimatedContent(
        targetState = screenState,
        transitionSpec = {
            fadeIn(animationSpec = tween(700)) with fadeOut(animationSpec = tween(700))
        }
    ) { state ->
        when {
            // Najpierw sprawdzamy, czy user nie wybrał ekranu logowania
            showLogin -> {
                // Nowa instancja AuthViewModel (jeśli tak chcesz)
                val authVmLocal: AuthViewModel = viewModel()
                AuthScreen(
                    onAuthenticated = {
                        // po zalogowaniu
                        showLogin = false
                        screenState = AppScreenState.Main
                    },
                    onBack = { showLogin = false },
                    viewModel = authVmLocal
                )
            }

            else -> {
                // Normalne stany (Loading, Main, Detail)
                when (state) {
                    AppScreenState.Loading -> {
                        // Ekran startowy (splash)
                        LoadingScreen(
                            onLoadingComplete = {
                                screenState = AppScreenState.Main
                            }
                        )
                    }

                    AppScreenState.Main -> {
                        MainScreen(
                            onLoginRequested = { showLogin = true },
                            viewModel = authVm,
                            onProductSelected = { product ->
                                // Przejście do ekranu szczegółów
                                screenState = AppScreenState.Detail(product)
                            }
                        )
                    }

                    is AppScreenState.Detail -> {
                        // Wyświetlamy ProductDetailScreen
                        ProductDetailScreen(
                            product = state.product,
                            onAddToCart = { addedProduct ->
                                // Ewentualnie: cartItems.add(addedProduct) // (lokalna lista, jeśli potrzebna)
                                val token = authVm.token
                                if (!token.isNullOrEmpty()) {
                                    // POPRAWKA: używamy addedProduct.id
                                    cartVm.addItem("Bearer $token", addedProduct.id, 1)
                                }
                                // Gdy chcesz wrócić do ekranu głównego:
                                screenState = AppScreenState.Main
                            },
                            onBack = {
                                // wracamy do MainScreen
                                screenState = AppScreenState.Main
                            }
                        )
                    }
                }
            }
        }
    }
}
