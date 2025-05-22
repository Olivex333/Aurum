package com.example.aurum.ui.screens.productdetail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.example.aurum.data.remote.ProductDto
import com.example.aurum.ui.screens.ascreen.poppinsFontFamily

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: ProductDto,
    onAddToCart: (ProductDto) -> Unit,
    onBack: () -> Unit
) {
    // Rozbijamy kolory i rozmiary na listy:
    val colorList = remember(product.colors) {
        product.colors.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
    val sizeList = remember(product.sizes) {
        product.sizes.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    // Stan wybranego koloru/rozmiaru
    var selectedColor by remember { mutableStateOf<String?>(colorList.firstOrNull()) }
    var selectedSize by remember { mutableStateOf<String?>(sizeList.firstOrNull()) }

    // „Slider” – 3 × to samo zdjęcie
    val images = listOf(
        product.image_url,
        product.image_url,
        product.image_url
    )

    // Dodajemy stan do pionowego scrolla
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Dzięki temu cały ekran jest pionowo przewijalny:
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // (A) Górny wiersz z przyciskiem "Wróć"
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Wróć"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Szczegóły produktu",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // (B) Slider (prosty) w LazyRow (poziome przewijanie zdjęć)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clipToBounds(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(images) { imgUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imgUrl),
                    contentDescription = "Product detail image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(300.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // (C) Nazwa i cena
        Text(
            text = product.name,
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        )
        Text(
            text = "€${product.price}",
            style = TextStyle(
                fontSize = 18.sp,
                color = Color.Gray,
                fontFamily = poppinsFontFamily
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // (D) Wybór koloru
        if (colorList.isNotEmpty()) {
            Text(
                text = "Kolor:",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(colorList) { colorItem ->
                    ElevatedFilterChip(
                        selected = (colorItem == selectedColor),
                        onClick = { selectedColor = colorItem },
                        label = { Text(colorItem) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // (E) Wybór rozmiaru
        if (sizeList.isNotEmpty()) {
            Text(
                text = "Rozmiar:",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sizeList) { sizeItem ->
                    ElevatedFilterChip(
                        selected = (sizeItem == selectedSize),
                        onClick = { selectedSize = sizeItem },
                        label = { Text(sizeItem) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // (F) Sekcja rozkładana: opis
        var expanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Szczegółowe informacje",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        if (expanded) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Zwiń")
                        } else {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Rozwiń")
                        }
                    }
                }
                if (expanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Opis: ${product.description}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // (G) Przycisk "Dodaj do koszyka"
        Button(
            onClick = {
                // tworzymy skopiowany obiekt z wybranym kolorem i rozmiarem
                val productToCart = product.copy(
                    chosenColor = selectedColor,
                    chosenSize = selectedSize
                )
                onAddToCart(productToCart)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(
                text = "Dodaj do koszyka",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            )
        }
    }
}
