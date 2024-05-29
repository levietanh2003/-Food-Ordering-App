package com.example.foodapp.Fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        return binding.root
    }

    private fun setUpOrderCustomer() {
        database = FirebaseDatabase.getInstance()
        val customerId = auth.currentUser?.uid ?: ""

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
//                    if (itemPushKey != null) {
//                        orderId.add(itemPushKey)
//                    }
//                    // Get currentTime
//                    val createAts = orderSnapshot.child("currentTime").getValue(Long::class.java)
//                    if (createAts != null) {
//                        createAt.add(createAts.toString())
//                    }
                }
                Log.d("OrderId","OrderIds : ${orderId.size}")
                Log.d("CreateAt","CreateAts : ${createAt.size}")
                Log.d("Delivery","Deliverys : ${deliveryStatus.size}")

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseData", "Failed to retrieve data orderInBuyHistory : ${error.message}")
            }
        })
    }


//    private fun setUpOrderCustomer() {
//        database = FirebaseDatabase.getInstance()
//        val customerId = auth.currentUser?.uid ?: ""
//
//        // vao bang BuyHistory lay don hang cua customer
//        val orderInBuyHistory: DatabaseReference =
//            database.reference.child("customer").child(customerId).child("BuyHistory")
//
//        orderId = mutableListOf()
//        createAt = mutableListOf()
//        deliveryStatus = mutableListOf()
//
//        orderInBuyHistory.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (orderSnapshot in snapshot.children) {
//                    // orderId
//                    val itemPushKey = snapshot.key
//                    if (itemPushKey != null) {
//                        orderId.add(itemPushKey)
//                    }
//                    // currentTime
//                    val createAts = snapshot.child("currentTime").getValue(String::class.java)
//                    if (createAts != null) {
//                        createAt.add(createAts)
//                    }
//
//                    val deliveryStatusInOrderDetails: DatabaseReference =
//                        database.reference.child("OrderDetails").child(itemPushKey.toString())
//                    deliveryStatusInOrderDetails.addListenerForSingleValueEvent(object :
//                        ValueEventListener {
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            for (deliveryStatusSnapshot in snapshot.children) {
//                                val deliveryStatusInDetails =
//                                    snapshot.child("deliveryStatus").getValue(String::class.java)
//                                if (deliveryStatusInDetails != null) {
//                                    deliveryStatus.add(deliveryStatusInDetails)
//                                }
//                            }
//
//                            Log.d("OrderId", "OrderId : ${orderId.size}")
//                            Log.d("CreateAt", "CreateAt : ${createAt.size}")
//                            Log.d("DeliveryStatus", "DeliveryStatus : ${deliveryStatus.size}")
//
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Log.e(
//                                "FirebaseData",
//                                "Failed to retrieve data deliveryStatusInOrderDetails : ${error.message}"
//                            )
//                        }
//
//                    })
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseData", "Failed to retrieve data orderInBuyHistory : ${error.message}")
//
//            }
//        })
//    }
}