package com.example.foodapp.Model

import java.util.Date

data class MenuItem(
    val foodId: String? = null,
    val foodName: String? = null,
    val foodPrice: String? = null,
    val foodDescription: String? = null,
    val foodImage: String? = null,
    val foodIngredient: String? = null,
    val typeOfDishId: String? = null,
    val trending: Boolean = false,
    val categoryId: String? = null,
    val bestSeller: Boolean = false,
    var createdAt: Long = 0,
    var endAt : Long = 0,
//    val discountValue : Double ?= null
//    val createAt : Date ?= null
)
