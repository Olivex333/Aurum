package com.example.aurum.ui.screens.ubr

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.aurum.R
import com.example.aurum.data.remote.ProductDto

// Enum z opcjami sortowania
enum class SortOption {
    NONE,
    PRICE_ASC,
    PRICE_DESC,
    NAME_ASC,
    NAME_DESC
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UbraniaScreen(
    viewModel: UbraniaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onProductClick: (ProductDto) -> Unit
) {
    // Obserwujemy stany z ViewModelu
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Stan wyszukiwania
    var searchQuery by remember { mutableStateOf("") }

    // Stan rozwijanego menu (SortOption)
    var sortOption by remember { mutableStateOf(SortOption.NONE) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    // Ładujemy produkty (tylko raz)
    LaunchedEffect(Unit) {
        viewModel.loadUbrania()
    }

    // --- FILTROWANIE I SORTOWANIE ---
    // Filtrowanie po nazwie
    val filteredProducts = products.filter { product ->
        product.name.contains(searchQuery, ignoreCase = true)
    }

    // Sortowanie w zależności od wybranej opcji
    val sortedProducts = remember(filteredProducts, sortOption) {
        when (sortOption) {
            SortOption.PRICE_ASC -> filteredProducts.sortedBy { it.price }
            SortOption.PRICE_DESC -> filteredProducts.sortedByDescending { it.price }
            SortOption.NAME_ASC -> filteredProducts.sortedBy { it.name.lowercase() }
            SortOption.NAME_DESC -> filteredProducts.sortedByDescending { it.name.lowercase() }
            else -> filteredProducts
        }
    }

    // Grupowanie po 2 w wierszu
    val rowItems = sortedProducts.chunked(2)

    // --- UI GŁÓWNE ---
    when {
        isLoading -> {
            // Ekran ładowania
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Ładowanie...", style = TextStyle(fontSize = 16.sp))
            }
        }
        error != null -> {
            // Ekran błędu
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = error ?: "Błąd", color = Color.Red)
            }
        }
        else -> {
            // Ekran listy ubrań
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
            ) {
                // (A) Pasek wyszukiwania + sortowanie
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pole tekstowe do wyszukiwania
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Szukaj produktu") },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Przycisk do wyświetlenia menu sortowania
                    Box {
                        Button(
                            onClick = { sortMenuExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text("Sortuj", color = Color.White)
                        }
                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Domyślnie") },
                                onClick = {
                                    sortOption = SortOption.NONE
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cena rosnąco") },
                                onClick = {
                                    sortOption = SortOption.PRICE_ASC
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cena malejąco") },
                                onClick = {
                                    sortOption = SortOption.PRICE_DESC
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Nazwa A–Z") },
                                onClick = {
                                    sortOption = SortOption.NAME_ASC
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Nazwa Z–A") },
                                onClick = {
                                    sortOption = SortOption.NAME_DESC
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // (B) Lista (LazyColumn) z nagłówkiem „Collection Summer '25”
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    stickyHeader {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Collection Summer '25",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    // Wyświetlamy wiersze z 2 produktami w każdym
                    items(rowItems) { pair ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            pair.forEach { product ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp)
                                        .clickable {
                                            onProductClick(product)
                                        }
                                ) {
                                    ClothingItemView(product)
                                }
                            }
                            // Jeśli wiersz ma tylko 1 element, dopełniamy pustym miejscem
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// Fonty i view do pojedynczego elementu (tak jak wcześniej)
val poppinsFontFamily2 = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun ClothingItemView(product: ProductDto) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(product.image_url),
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.name,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFontFamily2,
                color = Color.Black
            )
        )

        Text(
            text = product.colors,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = poppinsFontFamily2,
                color = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "€${product.price}",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily2,
                color = Color.Black
            )
        )

        Text(product.description)
    }
}
