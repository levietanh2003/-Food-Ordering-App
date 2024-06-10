package com.example.foodapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodapp.Fragment.CartFragment
import com.example.foodapp.databinding.FragmentCongratsBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratsBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratsBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCongratsBottomSheetBinding.inflate(layoutInflater,container,false)
        binding.btnGoHome.setOnClickListener {
//            val intent = Intent(requireContext(),MainActivity::class.java)
//            startActivity(intent)
            val refreshIntent = Intent(requireContext(), CartFragment::class.java)
            refreshIntent.putExtra("refreshCart", true)
            startActivity(refreshIntent)
        }
        return binding.root
    }

    companion object {
        
    }
}