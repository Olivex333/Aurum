package com.example.aurum.ui.screens.ascreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.aurum.R
import com.example.aurum.data.remote.ProductDto
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// Dodajemy param onProductClick
@Composable
fun AScreen(
    viewModel: ProductsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onProductClick: (ProductDto) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Statyczne obrazy do GŁÓWNEGO slidera
    val sliderImages = listOf(
        "https://polamarketing.com/wp-content/uploads/2024/06/On_Model-13@2x.jpg",
        "https://www.shutterstock.com/image-photo/summer-fashion-concept-woman-wearing-600nw-1789129787.jpg",
        "https://johngress.com/wp-content/uploads//2024/07/male-model-fashion-in-Chicago-David-20596-1_1200.jpg"
    )

    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Automatyczne przewijanie slidera
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % sliderImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    // Przy pierwszym wejściu na ekran – ładujemy produkty z backendu
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        // (1) Pierwszy item – duży slider + tytuł + paragraf
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                // Duży slider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.65f)
                ) {
                    HorizontalPager(
                        count = sliderImages.size,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = rememberAsyncImagePainter(sliderImages[page]),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Tytuł
                Text(
                    text = "SUMMER '25",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )

                // Paragraf
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                )
            }
        }

        // (2) Drugi item – tu wyświetlamy dane z backendu
        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Ładowanie...", style = TextStyle(fontSize = 16.sp))
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Błąd: $error", style = TextStyle(fontSize = 16.sp, color = Color.Red))
                }
            } else {
                // products to lista ProductDto z bazy
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(products) { product ->
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .background(Color.White)
                        ) {
                            ProductItemView(
                                product = product,
                                onItemClick = { clickedProduct ->
                                    onProductClick(clickedProduct)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItemView(
    product: ProductDto,
    onItemClick: (ProductDto) -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
            .clickable {
                onItemClick(product)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(product.image_url),
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Text(
            text = product.name,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFontFamily,
                color = Color.Black
            ),
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = product.colors,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily,
                color = Color.Gray
            )
        )

        Text(
            text = "€${product.price}",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily,
                color = Color.Black
            ),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
