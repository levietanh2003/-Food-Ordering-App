package com.example.foodapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHistoryFragmentBinding

class HistoryFragment : Fragment(R.layout.activity_history_fragment) {
    private lateinit var binding: ActivityHistoryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHistoryFragmentBinding.inflate(inflater)
        return binding.root
    }
}