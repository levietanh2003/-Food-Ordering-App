package com.example.foodapp.Model

data class Comment(
    var productId: String? = null,
    var comment: String = "",
    var createdAt: Long = 0,
    var customerID: String = "",
    var nameCustomer: String = "",
    var star: Float = 0f,
    var title: String = "",
)

