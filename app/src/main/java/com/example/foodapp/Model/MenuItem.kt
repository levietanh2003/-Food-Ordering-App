package com.example.foodapp.Model

import java.util.Date

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
    val discountValue : String ?= null
//    val discountValue : Double ?= null

//    val createAt : Date ?= null
)
