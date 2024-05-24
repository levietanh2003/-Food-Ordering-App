package com.example.foodapp.Model

data class CartItems(
    var foodName : String ?= null,
    var foodPrice : String ?= null,
    var foodDescription : String ?= null,
    var foodImage : String ?= null,
    var foodQuantity : Int ?= null,
    var foodIngredient: String ?=null,
    var typeOfDish: String ?= null,
    var category : String ?= null,
    val foodDiscount: String? = null
)
