package com.unindetec.qrmarina.ui.components

import android.util.Base64
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.unindetec.qrmarina.model.Usuario
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


// -------------------------------------------
// Clave de cifrado en HEX (AES-256-CBC)
// -------------------------------------------
private const val ENCRYPTION_KEY_HEX =
    "d3c7b2e1f5a948c6b0d91a8e2f47c90165b43d8c7a6e9f80a3b2c1d0e5f498a7"

// -------------------------------------------
// Función auxiliar para convertir HEX a ByteArray
// -------------------------------------------
fun hexStringToByteArray(s: String): ByteArray {
    val len = s.length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] = ((Character.digit(s[i], 16) shl 4)
                + Character.digit(s[i + 1], 16)).toByte()
        i += 2
    }
    return data
}

// -------------------------------------------
// Función para descifrar AES-256-CBC
// -------------------------------------------
fun decryptAes256Cbc(encryptedText: String): String {
    return try {
        val keyBytes = hexStringToByteArray(ENCRYPTION_KEY_HEX )
        val secretKey = SecretKeySpec(keyBytes, "AES")

        val parts = encryptedText.split(":")
        if (parts.size != 2) return "Formato inválido"

        val ivBytes = hexStringToByteArray(parts[0])
        val encryptedBytes = hexStringToByteArray(parts[1])
        val ivSpec = IvParameterSpec(ivBytes)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        String(decryptedBytes, Charsets.UTF_8)
    } catch (e: Exception) {
        Log.e("Decrypt", "Error al descifrar: ${e.message}")
        "Error al descifrar"
    }
}

fun decryptCryptoJsAesBase64(encryptedBase64: String): String {
    return try {
        val cipherData = Base64.decode(encryptedBase64, Base64.DEFAULT)
        val ivBytes = cipherData.copyOfRange(0, 16)
        val encryptedBytes = cipherData.copyOfRange(16, cipherData.size)

        val secretKey = SecretKeySpec(hexStringToByteArray(ENCRYPTION_KEY_HEX), "AES")
        val ivSpec = IvParameterSpec(ivBytes)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    } catch (e: Exception) {
        Log.e("Decrypt", "Error al descifrar: ${e.message}")
        "Error al descifrar"
    }
}


fun decryptToUsuario(encryptedBase64: String): Usuario? {

    return try {
        val decryptedString = decryptCryptoJsAesBase64(encryptedBase64)
        val usuarioJson = Gson().fromJson(decryptedString, Usuario::class.java)
        Usuario(
            grado = usuarioJson.grado,
            nombre = usuarioJson.nombre,
            fechaVigencia = usuarioJson.fechaVigencia,
            placas = usuarioJson.placas
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}