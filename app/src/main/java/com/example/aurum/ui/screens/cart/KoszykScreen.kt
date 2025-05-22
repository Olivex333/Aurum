package com.example.aurum.ui.screens.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.aurum.ui.screens.ascreen.poppinsFontFamily
import com.example.aurum.ui.screens.auth.AuthViewModel

@Composable
fun KoszykScreen(
    authViewModel: AuthViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),

) {
    val cartState by cartViewModel.cartState.collectAsState()

    LaunchedEffect(Unit) {
        val token = authViewModel.token
        if (!token.isNullOrEmpty()) {
            cartViewModel.loadCart("Bearer $token")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),  // tło ekranu
        contentAlignment = Alignment.Center
    ) {
        when (cartState) {
            is CartUiState.Idle -> {
                Text("Koszyk (Idle)", style = TextStyle(fontSize = 16.sp))
            }
            is CartUiState.Loading -> {
                Text("Ładowanie koszyka...", style = TextStyle(fontSize = 16.sp))
            }
            is CartUiState.Error -> {
                val msg = (cartState as CartUiState.Error).message
                Text("Błąd koszyka: $msg", color = Color.Red, fontSize = 16.sp)
            }
            is CartUiState.Success -> {
                val items = (cartState as CartUiState.Success).items

                if (items.isEmpty()) {
                    Text("Twój koszyk jest pusty", style = TextStyle(fontSize = 18.sp))
                } else {
                    // Kolumna obejmująca całą stronę
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Twój koszyk",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items) { cartItem ->
                                CartItemRow(
                                    name = cartItem.name,
                                    price = cartItem.price,
                                    quantity = cartItem.quantity,
                                    imageUrl = cartItem.image_url,
                                    chosenColor = cartItem.chosen_color,
                                    chosenSize = cartItem.chosen_size,
                                    onRemoveClick = {
                                        val token = authViewModel.token
                                        if (!token.isNullOrEmpty()) {
                                            cartViewModel.removeItem("Bearer $token", cartItem.cart_item_id)
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val total = items.sumOf { it.price * it.quantity }
                        // Dolny wiersz podsumowania
                        Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Razem:",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = poppinsFontFamily
                                )
                            )
                            Text(
                                text = "€%.2f".format(total),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppinsFontFamily
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                // Dalsze kroki (checkout)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(
                                text = "Przejdź do kasy",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CartItemRow(
    name: String,
    price: Double,
    quantity: Int,
    imageUrl: String,
    chosenColor: String? = null,
    chosenSize: String? = null,
    onRemoveClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // sekcja obrazek + tekst
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(MaterialTheme.shapes.small)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Medium)
                    Text("€$price", color = Color.Gray)
                    Text("Ilość: $quantity", color = Color.Gray)

                    // Jeśli w CartItemDto masz chosenColor / chosenSize
                    if (!chosenColor.isNullOrEmpty()) {
                        Text("Kolor: $chosenColor", color = Color.Gray)
                    }
                    if (!chosenSize.isNullOrEmpty()) {
                        Text("Rozmiar: $chosenSize", color = Color.Gray)
                    }
                }
            }

            // sekcja "usuń"
            IconButton(onClick = onRemoveClick) {
                Text("Usuń", color = Color.Red)
            }
        }
    }
}

