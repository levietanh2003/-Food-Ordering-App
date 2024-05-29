package com.example.foodapp.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.OrderAdapter
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityAllOrderFragmentBinding
import com.example.foodapp.databinding.ActivityOrderFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderFragment : Fragment() {

    private lateinit var binding: ActivityOrderFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var orderId: MutableList<String>
    private lateinit var deliveryStatus: MutableList<String>
    private lateinit var createAt: MutableList<String>
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityOrderFragmentBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        setUpOrderCustomer()

        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigate(R.id.action_orderFragment_to_profileFragment)
        }

        return binding.root
    }

    private fun setUpOrderCustomer() {
//        database = FirebaseDatabase.getInstance()
        val customerId = auth.currentUser?.uid ?: return

        // Access the BuyHistory node for the customer
        val orderInBuyHistory: DatabaseReference =
            database.reference.child("customer").child(customerId).child("BuyHistory")

        orderId = mutableListOf()
        createAt = mutableListOf()
        deliveryStatus = mutableListOf()

        orderInBuyHistory.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    // Get itemPushKey
                    val orderDetail = orderSnapshot.getValue(OrderDetails::class.java)

                    orderDetail?.itemPushKey?.let { orderId.add(it) }
                    orderDetail?.currentTime.toString()?.let { createAt.add(it) }
                    orderDetail?.deliveryStatus?.let { deliveryStatus.add(it) }

                }
                setAdapter()
                Log.d("OrderId", "OrderIds : ${orderId.size}")
                Log.d("CreateAt", "CreateAts : ${createAt.size}")
                Log.d("Delivery", "Deliverys : ${deliveryStatus.size}")

            }

            private fun setAdapter(){
                orderAdapter = OrderAdapter(
                    orderId,
                    createAt,
                    deliveryStatus,
                    requireContext()
                )
                binding.rvAllOrders.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvAllOrders.adapter = orderAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    "FirebaseData",
                    "Failed to retrieve data orderInBuyHistory : ${error.message}"
                )
            }
        })
    }
}