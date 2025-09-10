package com.unindetec.qrmarina.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.unindetec.qrmarina.ui.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.padding(16.dp),
        content = { padding ->
            Button(
                onClick = { navController.navigate(Screen.Scanner.route) },
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()

            ) {
                Text("Escanear QR")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
    val navController = rememberNavController() // controlador de navegación de prueba
    HomeScreen(navController = navController)

}
