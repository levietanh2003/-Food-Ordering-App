package com.example.foodapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.ActivityDetailsBinding
import java.text.NumberFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var foodName : String ?= null
    private var foodPrice : String ?= null
    private var foodDescription : String ?= null
    private var foodImage : String ?= null
    private var foodIngredient : String ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // khai bao anh xa
//        val foodName = intent.getStringExtra("FilteredMenuItems")
//        val foodImage = intent.getIntExtra("FilteredMenuItemImage", 0)

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")
//        binding.detailsFoodName.text = foodName
//        binding.detailsImageFood.setImageResource(foodImage)

        with(binding){
            detailsFoodName.text = foodName
            detailsDescription.text = foodDescription
            detailsIngredient.text = foodIngredient
            detailsPrice.text = formatPrice(foodPrice)
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailsImageFood)
        }

        // xy ly su kien back
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    private fun formatPrice(price: String?): String {
        return try {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            val parsedPrice = price?.toDouble() ?: 0.0
            numberFormat.format(parsedPrice)
        } catch (e: Exception) {
            "0 VNĐ" // Trả về mặc định nếu không thể định dạng giá
        }
    }
}