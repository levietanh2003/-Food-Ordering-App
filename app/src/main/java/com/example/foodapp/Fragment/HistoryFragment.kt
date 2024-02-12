package com.example.foodapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.BuyAgainAdapter
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHistoryFragmentBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: ActivityHistoryFragmentBinding
    private lateinit var historyAdapter: BuyAgainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHistoryFragmentBinding.inflate(layoutInflater,container,false)
        setUpBuyAgainRecyclerView()
        return binding.root
    }
    // load du lieu vao recycler
    private fun setUpBuyAgainRecyclerView() {
        val buyAgainFoodName = arrayListOf("Apple","Banana","Cherry")
        val buyAgainFoodPrice = arrayListOf("100.000","200.000","300.000")
        val buyAgainFoodImage = arrayListOf(R.drawable.food1,R.drawable.food2,R.drawable.food3)

        historyAdapter = BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage)
        binding.buyAgainRecyclerView.adapter = historyAdapter
    binding.buyAgainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}
