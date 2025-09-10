package com.unindetec.qrmarina.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.unindetec.qrmarina.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        content = { padding ->
            Button(
                onClick = { navController.navigate(Screen.Scanner.route) },
                modifier = androidx.compose.ui.Modifier.padding(padding)
            ) {
                Text("Abrir lector QR")
            }
        }
    )
}
