package com.example.foodapp.Model


data class MenuItem(
    val foodName: String? = null,
    var foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    val foodIngredient: String? = null,
    val typeOfDishId: String? = null,
    val trending: Boolean = false,
    val categoryId: String? = null,
    val bestSeller: Boolean = false,
    var discountValue : String ?= null
)
