package com.example.aurum.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.aurum.data.remote.ProductDto
import com.example.aurum.ui.components.BottomNavigationBar
import com.example.aurum.ui.screens.ascreen.AScreen
import com.example.aurum.ui.screens.auth.AuthViewModel
import com.example.aurum.ui.screens.cart.KoszykScreen
import com.example.aurum.ui.screens.menu.MenuScreen
import com.example.aurum.ui.screens.ubr.UbraniaScreen
import com.example.aurum.ui.screens.ustawienia.UstawieniaScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLoginRequested: () -> Unit,
    viewModel: AuthViewModel,
    onProductSelected: (ProductDto) -> Unit   // NOWY callback
) {
    var selectedTab by remember { mutableStateOf(MainScreenTab.A) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            when (selectedTab) {
                MainScreenTab.Menu -> {
                    MenuScreen()
                }
                MainScreenTab.Ubrania -> {
                    // UbraniaScreen też może mieć callback onProductClick
                    UbraniaScreen(
                        onProductClick = { product ->
                            onProductSelected(product)
                        }
                    )
                }
                MainScreenTab.A -> {
                    AScreen(
                        onProductClick = { product ->
                            onProductSelected(product)
                        }
                    )
                }
                MainScreenTab.Koszyk -> {
                    KoszykScreen()
                }
                MainScreenTab.Ustawienia -> {
                    UstawieniaScreen(
                        isLoggedIn = viewModel.currentUser != null,
                        currentUser = viewModel.currentUser,
                        onLoginRequested = onLoginRequested
                    )
                }
            }
        }
    }
}
