package com.example.foodapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.AllOrderAdapter
import com.example.foodapp.Adapter.BuyAgainAdapter
import com.example.foodapp.Help.formatPrice
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityAllOrderFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllOrderFragment : Fragment() {
    private lateinit var binding: ActivityAllOrderFragmentBinding

    private lateinit var allOrderAdapter: AllOrderAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var orderId: String
    private var listOfOrderItem: MutableList<OrderDetails> = arrayListOf()

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


        // Retrieve the orderId from arguments
        orderId = arguments?.getString("OrderDetailsId").toString()
        // Use the orderId as needed
        binding.tvOrderId.text = orderId

        // chuyen huong ve cac hoa don
        binding.imageCloseOrder.setOnClickListener {
            findNavController().navigate(R.id.action_allOrderFragment_to_orderFragment)
        }

        // set Order Customer
        setUpOrderCustomer(orderId)
        return binding.root

    }

    // xet trang thai cho don hang
    private fun updateStepView(deliveryStatus: String) {
        // Assuming stepView is being used to show delivery status steps
        val steps = listOf("Pending", "Delivering", "Done", "Cancel")

        if (deliveryStatus == "Cancel") {
            // Hide the step view and show the cancellation message
            binding.stepView.visibility = View.GONE
            binding.tvCancellationMessage.visibility = View.VISIBLE
        } else {
            // Show the step view and hide the cancellation message
            binding.stepView.visibility = View.VISIBLE
            binding.tvCancellationMessage.visibility = View.GONE

            // Set the steps in the step view
            binding.stepView.setSteps(steps)
            val statusIndex = when (deliveryStatus) {
                "Pending" -> 0
                "Delivering" -> 1
                "Done" -> 2
                else -> 0
            }
            binding.stepView.go(statusIndex, true)
        }
    }


    private fun setUpOrderCustomer(orderId: String) {
        showProgressbarAllOrders()
        val customerId = auth.currentUser?.uid ?: return
        Log.d("CustomerID", "AllOrderFragment: $customerId")
        listOfOrderItem = mutableListOf()

        val orderRef: DatabaseReference = database.reference.child("OrderDetails").child(orderId)

        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        val orderDetails = snapshot.getValue(OrderDetails::class.java)
                        if (orderDetails != null) {
                            hidenProgressbarAllOrders()
                            listOfOrderItem.add(orderDetails)
                            if (listOfOrderItem.isNotEmpty()) {
                                setPreviousBuyItemRecyclerView(orderDetails)
                                displayOrderDetails(orderDetails)
                            }
                        }

                        Log.d("AllOrder", "AllOrder : $listOfOrderItem")
                    } catch (e: DatabaseException) {
                        Log.e("FirebaseData", "Failed to convert item: ${e.message}")
                    }
                } else {
                    Log.d("OrderDetails", "No order found with the provided ID.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve data: ${error.message}")
            }
        })
    }

    private fun setPreviousBuyItemRecyclerView(orderDetails: OrderDetails) {
        val allOrderFoodName = orderDetails.foodNames ?: emptyList()
        val allOrderFoodPrice = orderDetails.foodPrices ?: emptyList()
        val allOrderQuantities = orderDetails.foodQuantities ?: emptyList()
        val allOrderImage = orderDetails.foodImage ?: emptyList()

        // Log the values
        for (i in allOrderFoodName.indices) {
            Log.d(
                "FoodApp",
                "Food Name: ${allOrderFoodName[i]}, Price: ${allOrderFoodPrice[i]}, Quantities: ${allOrderQuantities[i]}, Image: ${allOrderImage[i]}"
            )
        }

        val recyclerView = binding.rvProducts
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        allOrderAdapter = AllOrderAdapter(
            allOrderFoodName,
            allOrderFoodPrice,
            allOrderQuantities,
            allOrderImage,
            requireContext()
        )
        recyclerView.adapter = allOrderAdapter
    }

    private fun displayOrderDetails(orderDetails: OrderDetails) {
        binding.tvFullName.text = orderDetails.customerName
        binding.tvAddress.text = orderDetails.address
        binding.tvPhoneNumber.text = orderDetails.phoneNumber
        binding.tvTotalPrice.text = formatPrice(orderDetails.totalPrice)
        updateStepView(orderDetails.deliveryStatus.toString())
    }

    private fun showProgressbarAllOrders() {
        binding.progressbarAllOrders.visibility = View.VISIBLE
    }

    private fun hidenProgressbarAllOrders() {
        binding.progressbarAllOrders.visibility = View.GONE
    }
}