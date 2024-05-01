package com.example.foodapp

import MenuAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.databinding.FragmentMenuBootomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*

class MenuBootomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBootomSheetBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater,container,false)
        // load du thieu test recyclerView
        binding.btnBack.setOnClickListener {
            dismiss()
        }

        retrieveMenuItems()
        // btn back
        return binding.root
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems =  mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            // lang nghe su thay doi cua realtime data
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                // test du lieu len khong
                Log.d("ITEM","onDataChange: Data Received")
                // once data receive, set to adapter
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun setAdapter() {
        // kiem tra xem co san san pham khong
        if(menuItems.isNotEmpty()){
            val adapter = MenuAdapter(menuItems,requireContext())
            binding.menuRecyclerViews.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerViews.adapter = adapter
            Log.d("ITEM", "Setup Adapter : data set")
        }else{
            Log.d("ITEM", "Setup Adapter : data NOT set")
        }
    }
    companion object {

    }
}