package com.example.aurum.ui.screens.ustawienia

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aurum.data.remote.UserData
import com.example.aurum.ui.components.poppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UstawieniaScreen(
    isLoggedIn: Boolean,
    currentUser: UserData?,
    onLoginRequested: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Account",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoggedIn && currentUser != null) {
                Text(
                    text = "Witaj, ${currentUser.username}",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            } else {
                Text(
                    text = "Log in or sign up for a personalised shopping experience.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onLoginRequested,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(
                        text = "LOG IN / SIGN UP",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = poppinsFontFamily,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            AccountOptionItem("Moje dane")
            AccountOptionItem("Moje zamówienia")
            AccountOptionItem("Moje adresy ")
            AccountOptionItem("Lista życzeń")
            Spacer(modifier = Modifier.height(16.dp))
            AccountOptionItem("Kraj / Język")
            Spacer(modifier = Modifier.height(16.dp))
            AccountOptionItem("O nas ")
            AccountOptionItem("Polityka sklepu")
            AccountOptionItem("Dzial wsparcia")
            AccountOptionItem("FAQs")
        }
    }
}

@Composable
fun AccountOptionItem(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable {},
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily,
                color = Color.Black
            )
        )
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
