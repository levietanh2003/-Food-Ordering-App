package com.example.foodapp.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.CartAdapter
import com.example.foodapp.CongratsBottomSheet
import com.example.foodapp.PayOutAcitvity
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityCartFragmentBinding

class CartFragment : Fragment(R.layout.activity_cart_fragment) {
    private lateinit var binding: ActivityCartFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityCartFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load du thieu test recyclerView
        val foodNameCart = listOf("Canh Bí Đỏ","Tôm hùm hấp","Bánh Sandwich","Canh bi ngo")
        val price = listOf("270.000","270.000","270.000","270.000")
        val cartFoodImages = listOf(R.drawable.food1,R.drawable.food2,R.drawable.food3,R.drawable.food4)
        val adapter = CartAdapter(ArrayList(foodNameCart),ArrayList(price),ArrayList(cartFoodImages),requireContext())

        binding.recyclerViewCardFood.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCardFood.adapter = adapter

        // xu ly nut thanh toan
        binding.btnProceed.setOnClickListener {
            val intent = Intent(requireContext(), PayOutAcitvity::class.java)
            startActivity(intent)
        }
    }
    companion object{

    }
}