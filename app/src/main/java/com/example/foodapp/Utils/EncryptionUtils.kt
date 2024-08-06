package com.example.foodapp.Utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class EncryptionUtils {
    companion object {
        private const val ALGORITHM = "AES"

        // Tạo khóa bí mật cho AES
        fun generateSecretKey(): SecretKey {
            val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
            keyGenerator.init(256) // Khởi tạo khóa với độ dài 256 bit
            return keyGenerator.generateKey()
        }

        // Mã hóa dữ liệu bằng AES
        @RequiresApi(Build.VERSION_CODES.O)
        fun encrypt(data: String, secretKey: SecretKey): String {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            return Base64.getEncoder().encodeToString(encryptedBytes)
        }

        // Giải mã dữ liệu bằng AES
        @RequiresApi(Build.VERSION_CODES.O)
        fun decrypt(encryptedData: String, secretKey: SecretKey): String {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decodedBytes = Base64.getDecoder().decode(encryptedData)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            return String(decryptedBytes)
        }

        // Băm mật khẩu bằng SHA-256
        @RequiresApi(Build.VERSION_CODES.O)
        fun hashPassword(password: String): String {
            val md = MessageDigest.getInstance("SHA-256")
            val hashedBytes = md.digest(password.toByteArray())
            return Base64.getEncoder().encodeToString(hashedBytes)
        }
    }
}