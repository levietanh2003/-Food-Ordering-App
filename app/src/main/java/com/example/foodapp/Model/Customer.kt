package com.example.foodapp.Model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.foodapp.Utils.EncryptionUtils
import javax.crypto.SecretKey

data class Customer(
    var nameCustomer : String?=null,
    var emailCustomer : String?=null,
    var passwordCustomer : String?=null,
//    val profileImage: String? = null,
    var phoneNumberCustomer: String? = null,
    var addressCustomer: String? = null,
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptSensitiveData(secretKey: SecretKey) {
        nameCustomer = nameCustomer?.let { EncryptionUtils.encrypt(it, secretKey) }
        emailCustomer = emailCustomer?.let { EncryptionUtils.encrypt(it, secretKey) }
        passwordCustomer =
            passwordCustomer?.let { EncryptionUtils.hashPassword(it) } // Băm mật khẩu trước khi mã hóa
        phoneNumberCustomer = phoneNumberCustomer?.let { EncryptionUtils.encrypt(it, secretKey) }
        addressCustomer = addressCustomer?.let { EncryptionUtils.encrypt(it, secretKey) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decryptSensitiveData(secretKey: SecretKey) {
        nameCustomer = nameCustomer?.let { EncryptionUtils.decrypt(it, secretKey) }
        emailCustomer = emailCustomer?.let { EncryptionUtils.decrypt(it, secretKey) }
        phoneNumberCustomer = phoneNumberCustomer?.let { EncryptionUtils.decrypt(it, secretKey) }
        addressCustomer = addressCustomer?.let { EncryptionUtils.decrypt(it, secretKey) }
    }
}
