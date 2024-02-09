package com.example.foodapp.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}