package com.example.foodapp.Model

import java.util.*

data class FeedBack(
    val customerId : String?=null,
    val foodId: String?=null,
    val rating: Double,
    val comment: String,
    val createdAt: Date
)
