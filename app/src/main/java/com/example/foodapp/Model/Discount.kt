package com.example.foodapp.Model

import android.provider.ContactsContract.Data
import java.util.Date

data class Discount(
    val codeQR : String?=null,
    val start_Discount : Date,
    val end_Discount : Date,
    val createAt : Date,
    val updateAt : Date,
    val value : Double,
    val foodId : String?=null
)
