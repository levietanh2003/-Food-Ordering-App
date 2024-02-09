package com.example.foodapp.Fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodapp.Adapter.PopularAdapter
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var binding: ActivityHomeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHomeFragmentBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // luu danh sach image vao slider
        val imageListt = ArrayList<SlideModel>()
        imageListt.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageListt.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageListt.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imagesSlider = binding.imageSlider
        imagesSlider.setImageList(imageListt)
        imagesSlider.setImageList(imageListt, ScaleTypes.FIT)

        // cho du lieu test itemPopular
        val foodName = listOf("Canh Bí Đỏ","Tôm hùm hấp","Bánh Sandwich","Canh bi ngo")
        val price = listOf("270.000","270.000","270.000","270.000")
        val nameRetaurant = listOf("Nam Bộ","Nam Căn","Luxury","Sang bảnh")
        val popularFoodImages = listOf(R.drawable.food1,R.drawable.food2,R.drawable.food3,R.drawable.food4)
        val adapter = PopularAdapter(foodName,price,nameRetaurant,popularFoodImages)

        binding.popularRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter = adapter
    }
    companion object{

    }

}