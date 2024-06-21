package com.example.foodapp.Model

data class Redemptions(
    val voucher_id : String ?= null,
    val customerId : String ?= null,
    val redeemed_at : String ?= null
)
