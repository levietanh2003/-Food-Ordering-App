package com.example.foodapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.databinding.ActivityAllOrderFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllOrderFragment : Fragment() {
    private lateinit var binding: ActivityAllOrderFragmentBinding
    private lateinit var allOrders: MutableList<OrderDetails>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAllOrderFragmentBinding.inflate(inflater, container, false)

        // khoi tao data

        setUpOrderCustomer()
        return binding.root
    }

    private fun setUpOrderCustomer() {
        database = FirebaseDatabase.getInstance()
        val customerId = auth.currentUser?.uid ?: ""
        allOrders = mutableListOf()

        // truy xuat đơn hàng của customer
        val orderRef: DatabaseReference =
            database.reference.child("customer").child(customerId).child("BuyHistory")
        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredItems = mutableListOf<OrderDetails>()
                try {
                    for (orderSnapshot in snapshot.children) {
                        val allOrder = orderSnapshot.getValue(OrderDetails::class.java)
                        allOrder?.let {
                            filteredItems.add(it)
                        }
                    }
                } catch (e: DatabaseException) {
                    Log.e("FirebaseData", "Failed to convert item: ${e.message}")
                }
                allOrders = filteredItems
                Log.d("OrderDetails", "Item order: ${allOrders.size}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


    }
}