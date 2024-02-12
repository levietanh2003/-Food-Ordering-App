package com.example.foodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.CartAdapter
import com.example.foodapp.databinding.FragmentMenuBootomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBootomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBootomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater,container,false)
        // load du thieu test recyclerView
        val foodNameCart = listOf("Canh Bí Đỏ","Tôm hùm hấp","Bánh Sandwich","Canh bi ngo","Canh Bí Đỏ","Tôm hùm hấp","Bánh Sandwich","Canh bi ngo")
        val price = listOf("270.000","270.000","270.000","270.000","270.000","270.000","270.000","270.000")
        val cartFoodImages = listOf(R.drawable.food1
            ,R.drawable.food2
            ,R.drawable.food3
            ,R.drawable.food4
            ,R.drawable.food1
            ,R.drawable.food2
            ,R.drawable.food3
            ,R.drawable.food4)
        val adapter = CartAdapter(ArrayList(foodNameCart)
            ,ArrayList(price)
            ,ArrayList(cartFoodImages))

        binding.menuRecyclerViews.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerViews.adapter = adapter

        // btn back
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    companion object {

    }
}