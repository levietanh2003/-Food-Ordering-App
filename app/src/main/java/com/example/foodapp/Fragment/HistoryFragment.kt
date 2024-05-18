package com.example.foodapp.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.Adapter.BuyAgainAdapter
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.databinding.ActivityHistoryFragmentBinding
import com.example.foodapp.RecentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*

class HistoryFragment : Fragment() {
    private lateinit var binding: ActivityHistoryFragmentBinding
    private lateinit var historyAdapter: BuyAgainAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var customerId: String
    private var listOfOrderItem: MutableList<OrderDetails> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityHistoryFragmentBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        // Retrieve and display the Customer Order History
        retrieveBuyHistory()
//        setUpBuyAgainRecyclerView()

        // recent buy Button Clicks
        binding.recentBuyItem.setOnClickListener {
//            seeItemRecentBuy()
        }
        return binding.root
    }

    // function to see items recent buy
//    private fun seeItemRecentBuy() {
//        listOfOrderItem.firstOrNull()?.let { recentBuy ->
//            val intent = Intent(requireContext(), RecentOrderItems::class.java)
//            intent.putExtra("RecentBuyOrderItem", listOfOrderItem)
//            startActivity(intent)
//        }
//    }

    // fix loi truyen intent
//    private fun seeItemRecentBuy() {
//        if (listOfOrderItem.isNotEmpty()) {
//            val intent = Intent(requireContext(), RecentOrderItems::class.java)
//            intent.putParcelableArrayListExtra("RecentBuyOrderItem", listOfOrderItem)
//            startActivity(intent)
//        }
//    }


    // fun to retrieve items buy history
    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        customerId = auth.currentUser?.uid ?: ""

        val buyItemRef: DatabaseReference =
            database.reference.child("customer").child(customerId).child("BuyHistory")
        val shortingQuery = buyItemRef.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    // display the most recent order details
                    setDataInRecentBuyItem()
                    // setup to recyclerview with previous order details
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // fun display the most recent order details
    private fun setDataInRecentBuyItem() {
        binding.recentBuyItem.visibility = View.VISIBLE
        val recentBuyItem = listOfOrderItem.firstOrNull()
        recentBuyItem?.let {
            with(binding) {
                buyAgainFoodName1.text = it.foodNames?.firstOrNull() ?: ""
                buyAgainFoodPrice.text = formatPrice(it.foodPrices?.firstOrNull() ?: "")
                val image = it.foodImage?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)

                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
//                    setDataInRecentBuyItem()
//                    setPreviousBuyItemRecyclerView()
                }
            }
        }
    }

    // fun setup to recyclerview with previous order details
    private fun setPreviousBuyItemRecyclerView() {
        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOrderItem[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOrderItem[i].foodImage?.firstOrNull()?.let { buyAgainFoodImage.add(it) }
        }

        // Log the values
        for (i in buyAgainFoodName.indices) {
            Log.d(
                "FoodApp",
                "Food Name: ${buyAgainFoodName[i]}, Price: ${buyAgainFoodPrice[i]}, Image: ${buyAgainFoodImage[i]}"
            )
        }
        val recyclerView = binding.buyAgainRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = BuyAgainAdapter(
            buyAgainFoodName,
            buyAgainFoodPrice,
            buyAgainFoodImage,
            requireContext()
        )
        recyclerView.adapter = historyAdapter
    }

    private fun formatPrice(price: String?): String {
        return try {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            val parsedPrice = price?.toDouble() ?: 0.0
            numberFormat.format(parsedPrice)
        } catch (e: Exception) {
            "0 VNĐ" // Trả về mặc định nếu không thể định dạng giá
        }
    }
}
