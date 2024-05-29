package com.example.foodapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.AllOrderAdapter
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.databinding.ActivityAllOrderFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllOrderFragment : Fragment() {
    private lateinit var binding: ActivityAllOrderFragmentBinding
    private lateinit var allOrders: MutableList<OrderDetails>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var  orderId : String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityAllOrderFragmentBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        setUpOrderCustomer()
        return binding.root
    }

    // xet trang thai cho don hang
    private fun updateStepView(deliveryStatus: String) {
        // Assuming stepView is being used to show delivery status steps
        val steps = listOf("Pending", "Delivering", "Done", "Cancel")
        binding.stepView.setSteps(steps)

        val statusIndex = when (deliveryStatus) {
            "Pending" -> 0
            "Delivering" -> 1
            "Done" -> 2
            "Cancel" -> 3
            else -> 0
        }

        binding.stepView.go(statusIndex, true)
    }

    private fun setUpOrderCustomer() {
        database = FirebaseDatabase.getInstance()
        val customerId = auth.currentUser?.uid ?: ""
        Log.d("CustomerID","AllOrderFagment: $customerId")
        allOrders = mutableListOf()

        // truy xuat đơn hàng của customer
        val orderRef: DatabaseReference =
            database.reference.child("OrderDetails")
        val customerOrder = orderRef.orderByChild("customerId").equalTo(customerId)
        customerOrder.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val filteredItems = mutableListOf<OrderDetails>()
                try {
                    for (orderSnapshot in snapshot.children) {
                        val allOrder = orderSnapshot.getValue(OrderDetails::class.java)
                        allOrder?.let {
//                            val customerIdOrder  = snapshot.child("customerId").getValue(String::class.java)
                            orderId = snapshot.key.toString()
                            filteredItems.add(it)
                        }
                    }
                } catch (e: DatabaseException) {
                    Log.e("FirebaseData", "Failed to convert item: ${e.message}")
                }
                allOrders = filteredItems
                Log.d("ItemPushKey","ItemPushKey: $orderId")
                if (allOrders.isNotEmpty()) {
                    // Assuming you're displaying the first order details, you can change this logic as needed
                    displayOrderDetails(allOrders[0])
                }
                Log.d("OrderDetails", "Item order: ${allOrders.size}")
                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun displayOrderDetails(orderDetails: OrderDetails) {
        binding.tvOrderId.text = "#${orderId}"
        binding.tvFullName.text = orderDetails.customerName
        binding.tvAddress.text = orderDetails.address
        binding.tvPhoneNumber.text = orderDetails.phoneNumber
        binding.tvTotalPrice.text = "${orderDetails.totalPrice} VND"
        // dang chua co thuoc tinh
        updateStepView(orderDetails.deliveryStatus.toString())
    }

    private fun updateUI() {
        binding.rvProducts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvProducts.adapter = AllOrderAdapter(allOrders,requireContext())
    }

}