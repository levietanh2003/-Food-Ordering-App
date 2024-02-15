package com.example.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.Fragment.CartFragment
import com.example.foodapp.databinding.ActivityPayOutAcitvityBinding

class PayOutAcitvity : AppCompatActivity() {
    lateinit var binding: ActivityPayOutAcitvityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPayOutAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlaceMyOrder.setOnClickListener {
            val bottomSheetDeialog = CongratsBottomSheet()
            bottomSheetDeialog.show(supportFragmentManager,"Test")
        }
    }

}