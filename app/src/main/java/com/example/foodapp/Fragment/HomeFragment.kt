package com.example.foodapp.Fragment


import MenuAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapp.MenuBottomSheetFragment
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeFragmentBinding
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: ActivityHomeFragmentBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHomeFragmentBinding.inflate(inflater, container, false)
        // viewMenu
        binding.btnViewMore.setOnClickListener {
            val bottomSheetDialog = MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager, "Test")

        }

        var isMessengerButtonVisible: Boolean = true
        // setup food popular
        retrieveAndDisplayPopularItems()
        // set up food best seller
        retrieveAndDisPlayBestSellerItems()
        // set up Dish Type Button Click
        setDishTypeButtonClickListeners()

        binding.buttonMessenger.setOnClickListener {
            openMessenger()
        }

        binding.buttonClose.setOnClickListener {
            // Ẩn buttonMessenger
            binding.buttonMessenger.visibility = View.GONE
            binding.buttonClose.visibility = View.GONE

            // Cập nhật trạng thái của biến
            isMessengerButtonVisible = false
        }

        // Hiển thị hoặc ẩn buttonMessenger tùy theo giá trị của biến isMessengerButtonVisible
        if (isMessengerButtonVisible) {
            binding.buttonMessenger.visibility = View.VISIBLE
            binding.buttonClose.visibility = View.VISIBLE
        } else {
            binding.buttonMessenger.visibility = View.GONE
            binding.buttonClose.visibility = View.GONE
        }

        return binding.root
    }

    // Hàm xử lý khi nhấn vào các nút loại món ăn
    private fun setDishTypeButtonClickListeners() {
        binding.btnTypeOfDishCake.setOnClickListener {
            openBottomSheetWithDishType("CAKE")
        }

        binding.btnTypeOfDishBreads.setOnClickListener {
            openBottomSheetWithDishType("BREAD")
        }

        binding.btnTypeOfDishPastry.setOnClickListener {
            openBottomSheetWithDishType("PASTRY")
        }

        binding.btnTypeOfDishSandWishs.setOnClickListener {
            openBottomSheetWithDishType("SANDWICHES")
        }
    }

    // Hiện ProgressBarTrending
    private fun showProgressBarTrending() {
        binding.progressPopular.visibility = View.VISIBLE
    }

    // Ẩn ProgressBarTrending
    private fun hideProgressBarTrending() {
        binding.progressPopular.visibility = View.GONE
    }

    // Hiện ProgressBarBestSeller
    private fun showProgressBarBestSeller() {
        binding.progressBestSeller.visibility = View.VISIBLE
    }

    // Ẩn ProgressBarBestSeller
    private fun hideProgressBarBestSeller() {
        binding.progressBestSeller.visibility = View.GONE
    }

    // Hàm mở bottom sheet với loại món ăn được chọn
    private fun openBottomSheetWithDishType(typeOfDish: String) {
        val bottomSheetDialog = MenuBottomSheetFragment()
        val bundle = Bundle()
        bundle.putString("typeOfDish", typeOfDish)
        bottomSheetDialog.arguments = bundle
        bottomSheetDialog.show(parentFragmentManager, "Test")
    }

    private fun openMessenger() {
        val pageId = "273076119232591"
        val uri = Uri.parse("https://m.me/$pageId")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            startActivity(intent)
            Log.d("Messenger", "Opening Messenger in browser: $uri")
        } catch (e: Exception) {
            Log.e("Messenger", "Failed to open Messenger: ${e.message}")
            Toast.makeText(requireContext(), "Failed to open Messenger", Toast.LENGTH_SHORT).show()
        }
    }

    private fun retrieveAndDisPlayBestSellerItems() {
        showProgressBarBestSeller()
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        // truy xuat san pham nao co  thuoc tinh bestSeller bang true
        val foodBestSeller = foodRef.orderByChild("bestSeller").equalTo(true)

        foodBestSeller.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredItems = mutableListOf<MenuItem>()

                for (foodSnapshot in snapshot.children) {
//                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
//                    menuItem?.let { menuItems.add(it) }
                    try {
                        val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                        menuItem?.let {
                            // Assign the snapshot key to foodId
                            it.foodId = foodSnapshot.key
                            // discount
                            val discountValue =
                                foodSnapshot.child("discount").getValue(String::class.java)
                            if (discountValue != null) {
                                it.discountValue = discountValue
                            }
                            // created At
                            val createdAt =
                                foodSnapshot.child("createAt").getValue(String::class.java)
                            if (createdAt != null) {
                                it.createdAt = createdAt
                            }
                            // end At
                            val endAt = foodSnapshot.child("endAt").getValue(String::class.java)
                            if (endAt != null) {
                                it.endAt = endAt
                            }
                            // inStock
                            val inStock =
                                foodSnapshot.child("inStock").getValue(Boolean::class.java)
                            if (inStock == true) {
                                filteredItems.add(it)
                            }
                            Log.d("CreateAt", "Created At : $createdAt")
                            Log.d("EndAt", "End At : $endAt")
                            Log.d("FoodId", "Food Id : ${it.foodId}")

                        }
                    } catch (e: DatabaseException) {
                        Log.e("FirebaseData", "Failed to convert item: ${e.message}")
                    }
                }
                Log.d("FirebaseData", "Number of items best seller: ${menuItems.size}")

                menuItems = filteredItems
                randomBestSellerItems()
                hideProgressBarBestSeller()
                binding.progressBestSeller.visibility = View.GONE
                binding.bestSellerRecyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve data best seller: ${error.message}")
            }
        })
    }

    private fun retrieveAndDisplayPopularItems() {
        showProgressBarTrending()
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        val foodPopular = foodRef.orderByChild("trending").equalTo(true)

        foodPopular.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredItems = mutableListOf<MenuItem>()

                for (foodSnapshot in snapshot.children) {
                    try {
                        val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                        menuItem?.let {
                            // Assign the snapshot key to foodId
                            it.foodId = foodSnapshot.key
                            //inStock
                            val inStock =
                                foodSnapshot.child("inStock").getValue(Boolean::class.java)
                            // discountValue
                            val discountValue =
                                foodSnapshot.child("discount").getValue(String::class.java)
                            if (discountValue != null) {
                                it.discountValue = discountValue
                            }
                            // createdAt
                            val createdAt =
                                foodSnapshot.child("createAt").getValue(String::class.java)
                            if (createdAt != null) {
                                it.createdAt = createdAt
                            }
                            // endAt
                            val endAt =
                                foodSnapshot.child("endAt").getValue(String::class.java)
                            if (endAt != null) {
                                it.endAt = endAt
                            }

                            if (inStock == true) {
                                filteredItems.add(it)
                            }
                            Log.d("CreateAt", "Created At : $createdAt")
                            Log.d("EndAt", "End At : $endAt")
                            Log.d("FoodId", "FoodId : ${it.foodId}")

                        }
                    } catch (e: DatabaseException) {
                        Log.e("FirebaseData", "Failed to convert item: ${e.message}")
                    }
                }
                menuItems = filteredItems
                randomPopularItems()
                hideProgressBarTrending()
                binding.progressPopular.visibility = View.GONE
                binding.popularRecyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve data trending: ${error.message}")
            }
        })
    }


    private fun randomBestSellerItems() {
        val index = menuItems.indices.toList().shuffled()
        // so san pham hien khi bat dau
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }
        setBestSellerItemsAdapter(subsetMenuItems)
    }

    private fun randomPopularItems() {
        val index = menuItems.indices.toList().shuffled()
        // so san pham hien khi bat dau
        val numItemToShow = 6
        val subsetMenuItems = index.take(numItemToShow).map { menuItems[it] }
        setPopularItemsAdapter(subsetMenuItems)
    }

    // setup adapter
    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

    private fun setBestSellerItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())
        binding.bestSellerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.bestSellerRecyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // luu danh sach image vao slider
        val imageListt = ArrayList<SlideModel>()
        imageListt.add(SlideModel(R.drawable.banner_break, ScaleTypes.FIT))
        imageListt.add(SlideModel(R.drawable.banner2_break, ScaleTypes.FIT))
        imageListt.add(SlideModel(R.drawable.banner3_break, ScaleTypes.FIT))

        val imagesSlider = binding.imageSlider
        imagesSlider.setImageList(imageListt)
        imagesSlider.setImageList(imageListt, ScaleTypes.FIT)

    }
}