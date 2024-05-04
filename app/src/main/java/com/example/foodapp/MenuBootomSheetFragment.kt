package com.example.foodapp

import MenuAdapter
import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.databinding.FragmentMenuBootomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*

class MenuBootomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMenuBootomSheetBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>
    private var typeOfDish: String? = null
    private lateinit var categories: MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater, container, false)
        arguments?.let {
            // Nhận dữ liệu loại món ăn từ Bundle
            typeOfDish = it.getString("typeOfDish")
        }
        // load du thieu test recyclerView
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        // set up sinper categori
//        loadCategories()

        retrieveMenuItems()
        // btn back
        return binding.root
    }

    private fun retrieveMenuItems() {

        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        // Lọc các món ăn dựa trên loại món ăn
        val query = if (!typeOfDish.isNullOrEmpty()) {
            foodRef.orderByChild("typeOfDishId").equalTo(typeOfDish)
        } else {
            foodRef // Nếu không có loại món ăn cụ thể, lấy tất cả các món ăn
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            // Lắng nghe sự thay đổi trong dữ liệu thời gian thực
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                // Kiểm tra dữ liệu nhận được
                Log.d("ITEM", "onDataChange: Dữ liệu nhận được: ${menuItems.size}")
                // Sau khi nhận được dữ liệu, thiết lập adapter
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi cơ sở dữ liệu
                Log.e("FirebaseData", "Failed to retrieve data type of dish : ${menuItems.size}")
            }
        })
    }

    private fun setAdapter() {
        // kiem tra xem co san san pham khong
        if (menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.menuRecyclerViews.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerViews.adapter = adapter
            Log.d("ITEM", "Setup Adapter : data set")
        } else {
            Log.d("ITEM", "Setup Adapter : data NOT set")
        }
    }

    private fun loadCategories() {
        val database = FirebaseDatabase.getInstance()
        val categoryList = mutableListOf<String>()
        val categoryRef = database.reference.child("categories")

        categoryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Xóa danh sách cũ trước khi cập nhật


                // Duyệt qua snapshot để lấy danh sách các danh mục
                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.child("category_name").getValue(String::class.java)
                    categoryName?.let { categoryList.add(it) }
                }
                Log.d("Data","Data : ${categoryList.size}")

                // Cập nhật spinner (hoặc các thành phần giao diện khác) với danh sách mới
                updateCategorySpinner()
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }
    private fun updateCategorySpinner() {
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }


    companion object {

    }
}