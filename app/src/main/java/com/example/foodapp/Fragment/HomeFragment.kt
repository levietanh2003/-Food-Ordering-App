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
import com.example.foodapp.Adapter.PopularAdapter
import com.example.foodapp.MenuBootomSheetFragment
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeFragmentBinding
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: ActivityHomeFragmentBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems : MutableList<MenuItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHomeFragmentBinding.inflate(inflater,container,false)
        // viewMenu
        binding.btnViewMore.setOnClickListener {
            val bottomSheetDialog = MenuBootomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }

        // setup food popular
        retrieveAndDisplayPopularItems()
        // set up food best seller
        retrieveAndDisPlayBestSellerItems()
        return binding.root
    }

    private fun retrieveAndDisPlayBestSellerItems() {
      database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        // truy xuat san pham nao co  thuoc tinh bestSeller bang true
        var foodBestSeller = foodRef.orderByChild("bestSeller").equalTo(true)

        foodBestSeller.addListenerForSingleValueEvent( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                Log.d("FirebaseData", "Number of items best seller: ${menuItems.size}")

                randomPopularItems()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve data best seller: ${error.message}")
            }
        })
    }

    private fun retrieveAndDisplayPopularItems() {
        // get reference to the database
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        // truy xuất sản phẩm nào có thuộc tính trending bằng true mới hiên lên
        var foodPopuar = foodRef.orderByChild("trending").equalTo(true)

        foodPopuar.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                // test nhan du lieu va dem so luong trong list
                Log.d("FirebaseData", "Number of items received trending: ${menuItems.size}")

                // ham hien ngau nhien san pham
                randomBestSellerItems()
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
        val adapter = MenuAdapter(subsetMenuItems,requireContext())
        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }

    private fun setBestSellerItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems,requireContext())
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