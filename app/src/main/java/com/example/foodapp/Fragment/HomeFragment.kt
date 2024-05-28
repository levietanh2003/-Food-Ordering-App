package com.example.foodapp.Fragment


import MenuAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // setup food popular
        retrieveAndDisplayPopularItems()
        // set up food best seller
        retrieveAndDisPlayBestSellerItems()
        // set up Dish Type Button Click
        setDishTypeButtonClickListeners()

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

    // Hàm mở bottom sheet với loại món ăn được chọn
    private fun openBottomSheetWithDishType(typeOfDish: String) {
        val bottomSheetDialog = MenuBottomSheetFragment()
        val bundle = Bundle()
        bundle.putString("typeOfDish", typeOfDish)
        bottomSheetDialog.arguments = bundle
        bottomSheetDialog.show(parentFragmentManager, "Test")
    }

    private fun retrieveAndDisPlayBestSellerItems() {
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
                            if(endAt != null){
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
                binding.progressBestSeller.visibility = View.GONE
                binding.bestSellerRecyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve data best seller: ${error.message}")
            }
        })
    }

    private fun retrieveAndDisplayPopularItems() {
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