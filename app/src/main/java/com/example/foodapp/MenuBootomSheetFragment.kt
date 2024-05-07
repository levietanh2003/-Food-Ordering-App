package com.example.foodapp

import MenuAdapter
import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
    private var selectedCategory: String? = null

    // Thêm biến để theo dõi cách sắp xếp hiện tại
    private var currentSortOption: Int = SORT_BY_NONE

    // Các hằng số để biểu diễn các tùy chọn sắp xếp
    private companion object {
        const val SORT_BY_NONE = 0
        const val SORT_BY_PRICE_LOW_TO_HIGH = 1
        const val SORT_BY_PRICE_HIGH_TO_LOW = 2
    }

    // sap xep food theo gia tien
    private fun sortMenuItems() {
        when (currentSortOption) {
            SORT_BY_PRICE_LOW_TO_HIGH -> {
                menuItems.sortBy { it.foodPrice?.toInt() }
                // lod kiem tra ket qua tra ve
                menuItems.forEach { menuItem ->
                    Log.d("SortedMenuItem", "Food Price: ${menuItem.foodPrice}")
                }
            }
            SORT_BY_PRICE_HIGH_TO_LOW -> {
                menuItems.sortByDescending { it.foodPrice?.toInt() }
                // lod kiem tra ket qua tra ve
                menuItems.forEach { menuItem ->
                    Log.d("SortedMenuItem", "Food Price: ${menuItem.foodPrice}")
                }
            }
        }
        setAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater, container, false)
        arguments?.let {
            typeOfDish = it.getString("typeOfDish")
        }
        setupViews()
        loadCategories()
        retrieveMenuItems()

        // btn sap xep food theo gia
        setupSortOptions()

        return binding.root
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            dismiss()
        }
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = parent?.getItemAtPosition(position) as? String
                    retrieveMenuItems()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
    }

    private fun setupSortOptions() {
        // Xử lý sự kiện khi người dùng chọn sắp xếp theo giá thấp đến cao
        binding.btnSortLowToHigh.setOnClickListener {
            currentSortOption = SORT_BY_PRICE_LOW_TO_HIGH
            sortMenuItems()
        }

        // Xử lý sự kiện khi người dùng chọn sắp xếp theo giá cao đến thấp
        binding.btnSortHighToLow.setOnClickListener {
            currentSortOption = SORT_BY_PRICE_HIGH_TO_LOW
            sortMenuItems()
        }

        // Xử lý sự kiện khi người dùng muốn bỏ chọn sắp xếp
        binding.btnSortNone.setOnClickListener {
            currentSortOption = SORT_BY_NONE
            retrieveMenuItems() // Lấy lại dữ liệu món ăn gốc
        }
    }

    private fun retrieveMenuItems() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        val query =
            if (!typeOfDish.isNullOrEmpty()) {
                foodRef.orderByChild("typeOfDishId").equalTo(typeOfDish)
            } else if (!selectedCategory.isNullOrEmpty()) {
                foodRef.orderByChild("categoryId").equalTo(selectedCategory)
            } else {
                foodRef
            }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for (foodSnapshot in snapshot.children) {

                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        // Kiểm tra xem món ăn có sẵn trong kho không (inStock là true)
                        val inStock = foodSnapshot.child("inStock").getValue(Boolean::class.java)
                        if (inStock == true) {
                            menuItems.add(it)
                        }
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve menu items: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.menuRecyclerViews.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerViews.adapter = adapter
            Log.d("ITEM", "Setup Adapter: data set")
        } else {
            Log.d("ITEM", "Setup Adapter: data NOT set")
        }
    }

    private fun loadCategories() {
        database = FirebaseDatabase.getInstance()
        val categoryRef = database.reference.child("categories")
        val categories = mutableListOf<String>()

        categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categories.clear()
                for (categorySnapshot in snapshot.children) {
                    val categoryName =
                        categorySnapshot.child("category_name").getValue(String::class.java)
                    categoryName?.let { categories.add(it) }
                }
                updateCategorySpinner(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve categories: ${error.message}")
            }
        })
    }

    private fun updateCategorySpinner(categories: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }
}
