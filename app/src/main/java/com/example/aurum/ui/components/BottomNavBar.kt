package com.example.aurum.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.aurum.R
import com.example.aurum.ui.screens.main.MainScreenTab

val poppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun BottomNavigationBar(selectedTab: MainScreenTab, onTabSelected: (MainScreenTab) -> Unit) {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = selectedTab == MainScreenTab.Menu,
            onClick = { onTabSelected(MainScreenTab.Menu) },
            icon = {
                Text(
                    "Menu",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black
                    )
                )
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFF0F0F0),
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.Black
            )
        )
        NavigationBarItem(
            selected = selectedTab == MainScreenTab.Ubrania,
            onClick = { onTabSelected(MainScreenTab.Ubrania) },
            icon = {
                Text(
                    "Ubrania",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black
                    )
                )
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFF0F0F0),
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.Black
            )
        )
        NavigationBarItem(
            selected = selectedTab == MainScreenTab.A,
            onClick = { onTabSelected(MainScreenTab.A) },
            icon = {
                Text(
                    "A",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 24.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFF0F0F0),
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.Black
            )
        )
        NavigationBarItem(
            selected = selectedTab == MainScreenTab.Koszyk,
            onClick = { onTabSelected(MainScreenTab.Koszyk) },
            icon = {
                Text(
                    "Koszyk",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black
                    )
                )
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFF0F0F0),
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.Black
            )
        )
        NavigationBarItem(
            selected = selectedTab == MainScreenTab.Ustawienia,
            onClick = { onTabSelected(MainScreenTab.Ustawienia) },
            icon = {
                Text(
                    "Ustawienia",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 10.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Black
                    )
                )
            },
            label = null,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFFF0F0F0),
                selectedIconColor = Color.Black,
                unselectedIconColor = Color.Black
            )
        )
    }
}
