package com.unindetec.qrmarina.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.unindetec.qrmarina.model.Usuario
import com.unindetec.qrmarina.ui.components.decryptAes256Cbc
import com.unindetec.qrmarina.ui.components.decryptCryptoJsAesBase64
import com.unindetec.qrmarina.ui.components.decryptToUsuario
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(navController: NavController) {
    var qrResult by remember { mutableStateOf<String?>(null) }
    var usuario by remember { mutableStateOf<Usuario?>(null) }

    val launcher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            qrResult = result.contents
            try {
                usuario = decryptToUsuario(result.contents)
            } catch (e: Exception) {
                Log.e("Decrypt Error", e.message ?: "Error desconocido")
                usuario = null
            }
        } else {
            qrResult = "Cancelado"
            usuario = null
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
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Escáner QR"
                    )
                }
            )
        },
        content = { paddingValues ->
            // Aplicamos fillMaxSize y respetamos el padding del Scaffold
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                if (usuario != null) {
                    Text(
                        text = "Grado: ${usuario!!.grado}",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()

                    )
                    Text(
                        text = "Nombre: ${usuario!!.nombre}",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()

                    )
                    Text(text = "Fecha vigencia: ${usuario!!.fechaVigencia}",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .fillMaxWidth()
                    )
                    Text(text = "Placas: ${usuario!!.placas}",
                        modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth()
                    )
                } else if (qrResult != null) {
                    Text(text = "Descifrando...", modifier = Modifier.fillMaxWidth())
                } else {
                    Text(
                        text = "Abriendo cámara...",
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    )
}




@Preview(showBackground = true)
@Composable
fun PreviewScannerScreen(){
    val navController = rememberNavController() // controlador de navegación de prueba
    ScannerScreen(navController = navController)

}
