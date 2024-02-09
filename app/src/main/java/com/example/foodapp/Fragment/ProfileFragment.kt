package com.example.foodapp.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityProfileFragmentBinding

class ProfileFragment : Fragment(R.layout.activity_profile_fragment) {
    private lateinit var binding: ActivityProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityProfileFragmentBinding.inflate(inflater)
        return binding.root
    }
}