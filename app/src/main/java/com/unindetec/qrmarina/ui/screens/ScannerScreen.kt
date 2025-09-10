package com.unindetec.qrmarina.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {
    var qrResult by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            qrResult = result.contents
        } else {
            qrResult = "Cancelado"
        }
    }

    LaunchedEffect(Unit) {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Escanea un código QR")
        options.setCameraId(0)
        options.setBeepEnabled(true)
        options.setBarcodeImageEnabled(true)
        launcher.launch(options)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Escáner QR") }) },
        content = {
            if (qrResult != null) {
                Text(
                    text = "Resultado: $qrResult",
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = "Abriendo cámara...",
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )
            }
        }
    )
}
