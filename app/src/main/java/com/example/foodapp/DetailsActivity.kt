package com.example.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // khai bao anh xa
        val foodName = intent.getStringExtra("FilteredMenuItems")
        val foodImage = intent.getIntExtra("FilteredMenuItemImage", 0)

        // gan gia tri
        binding.detailsFoodName.text = foodName
        binding.detailsImageFood.setImageResource(foodImage)

        // xy ly su kien back
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}