package com.example.foodapp.Help

import java.text.NumberFormat
import java.util.*

fun formatPrice(price: String?): String {
    return try {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val parsedPrice = price?.toDouble() ?: 0.0
        numberFormat.format(parsedPrice)
    } catch (e: Exception) {
        "0 VNĐ" // Trả về mặc định nếu không thể định dạng giá
    }
}