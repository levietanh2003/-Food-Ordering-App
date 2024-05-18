package com.example.foodapp.Model

data class Comment(
    var comment: String = "",
    var createdAt: Long = 0,
    var customerId: String = "",
    var star: Float = 0f,
    var title: String = "",
    var foodComment: String? = null,
)

