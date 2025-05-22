package com.example.aurum.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun MenuScreen() {
    val poppinsFontFamily = FontFamily(
        Font(com.example.aurum.R.font.poppins_regular, FontWeight.Normal),
        Font(com.example.aurum.R.font.poppins_medium, FontWeight.Medium),
        Font(com.example.aurum.R.font.poppins_bold, FontWeight.Bold)
    )

    val menuItems = listOf(
        "AURUM - Ubrania",
        "Nowości",
        "SUMMER '25",
        "Klub",
        "Bestsellery",
        "Kolekcje",
        "Obuwie"
    )

    val sliderImages = listOf(
        "https://polamarketing.com/wp-content/uploads/2024/06/On_Model-13@2x.jpg",
        "https://www.shutterstock.com/image-photo/summer-fashion-concept-woman-wearing-600nw-1789129787.jpg",
        "https://johngress.com/wp-content/uploads//2024/07/male-model-fashion-in-Chicago-David-20596-1_1200.jpg"
    )

    val pagerState = rememberPagerState(initialPage = 0)

    // Automatyczna zmiana obrazu co 5 sekund w sliderze
    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % sliderImages.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = { /* TODO: Handle search logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Szukaj", fontFamily = poppinsFontFamily, color = Color.Gray) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            )
        )

        // Lista pozycji w menu
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            items(menuItems) { item ->
                Text(
                    text = item,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* TODO: Handle click */ }
                        .padding(vertical = 12.dp)
                )
            }
        }

        // Slider na dole ekranu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            HorizontalPager(
                count = sliderImages.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Image(
                    painter = rememberAsyncImagePainter(sliderImages[page]),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Napis "SUMMER '25" w lewym dolnym rogu
            Text(
                text = "SUMMER '25",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
