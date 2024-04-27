package com.example.foodapp.Fragment

import MenuAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivitySearchFragmentBinding

class SearchFragment : Fragment() {
    private lateinit var binding: ActivitySearchFragmentBinding
    private lateinit var adapter: MenuAdapter
    private val originalMenuFoodName = listOf(
        "Burger",
        "Pizza",
        "Sandwich",
        "Pasta",
        "Salad",
        "Chicken",
    )

    private val originalMenuPrice = listOf(
        "270.000",
        "360.000",
        "250.000",
        "50.000",
        "300.000",
        "400.000",
    )
    private val originalMenuImage = listOf(
        R.drawable.food1,
        R.drawable.food2,
        R.drawable.food3,
        R.drawable.food4,
        R.drawable.food1,
        R.drawable.food2,
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySearchFragmentBinding.inflate(inflater, container, false)
//        adapter = MenuAdapter(originalMenuFoodName.toMutableList(),
//            originalMenuPrice.toMutableList(),
//            originalMenuImage.toMutableList(),
//            requireContext())
        binding.menuRecyclerViews.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerViews.adapter = adapter
        setUpSearchView()
        return binding.root
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }
}
