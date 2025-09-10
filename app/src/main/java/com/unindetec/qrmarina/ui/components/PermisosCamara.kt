package com.unindetec.qrmarina.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PermisosCamara(navController: NavController) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
    }

    // Lanzar el permiso automáticamente si no se tiene
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCameraPermission) {
        // Navega a la pantalla principal o escáner
        navController.navigate("home") {
            popUpTo("permission") { inclusive = true }
        }
    } else {
        // Mensaje mientras el usuario concede permiso
        Text(
            "Se necesita permiso de cámara para escanear QR",
            modifier = androidx.compose.ui.Modifier.padding(16.dp)
        )
    }
}
