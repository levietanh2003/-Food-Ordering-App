package com.example.foodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.NotificationAdapter
import com.example.foodapp.databinding.FragmentNotifactionBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Notifaction_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotifactionBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotifactionBottomBinding.inflate(inflater,container,false)
        setUpNotificationAdapter()
        return binding.root
    }

    private fun setUpNotificationAdapter() {
        val notificationText = listOf("Your order has been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationImage = listOf(R.drawable.sademoji,R.drawable.ic_truck,R.drawable.congrats)
        val adapter = NotificationAdapter(ArrayList(notificationText),ArrayList(notificationImage))
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter = adapter

    }

    companion object {

    }
}