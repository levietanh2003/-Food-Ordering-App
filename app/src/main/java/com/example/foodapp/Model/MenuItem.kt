package com.example.foodapp.Model

data class MenuItem(
    val foodName : String ?= null,
    val foodPrice : String ?= null,
    val foodDescription : String ?= null,
    val foodImage : String ?= null,
    val foodIngredient : String ?= null,
    val typeOfDishId : String ?= null
)
