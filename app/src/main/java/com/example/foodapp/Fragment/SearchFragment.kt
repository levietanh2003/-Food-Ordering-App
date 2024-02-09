package com.example.foodapp.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivitySearchFragmentBinding

class SearchFragment : Fragment(R.layout.activity_search_fragment) {
    private lateinit var binding: ActivitySearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySearchFragmentBinding.inflate(inflater)
        return binding.root
    }
}