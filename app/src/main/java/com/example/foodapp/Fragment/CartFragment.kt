package com.example.foodapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.CartAdapter
import com.example.foodapp.Model.CartItems
import com.example.foodapp.PayOutAcitvity
import com.example.foodapp.databinding.ActivityCartFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : Fragment() {
    private lateinit var binding: ActivityCartFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImagesUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var customerId: String
    private lateinit var typeOfDish: MutableList<String>
    private lateinit var totalAmount: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityCartFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        retrieveCartItems()

        // xu ly nut thanh toan
        binding.btnProceed.setOnClickListener {
            // get order items details before proceeding to check out
            getItemOrderDetail()
        }
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

    private fun getItemOrderDetail() {
//       val orderIdRef : DatabaseReference = database.reference.child("'customer").child(customerId).child("CartItems")
        val foodQuantiles = cartAdapter.getUpdateItemsQuantities()
        val foodDetails = mutableListOf<Triple<String, String, String>>()

        for (i in 0 until cartAdapter.itemCount) {
            val foodDetail = cartAdapter.getUpdateItemFood(i)
            foodDetails.add(foodDetail)
        }

        val foodNames = mutableListOf<String>()
        val foodPrices = mutableListOf<String>()
        val foodImages = mutableListOf<String>()

        // Lấy thông tin từ danh sách foodDetails
        for (detail in foodDetails) {
            foodNames.add(detail.first)
            foodPrices.add(detail.second)
            foodImages.add(detail.third)
        }

        orderNow(foodNames, foodPrices, foodQuantiles, foodImages)

    }

    private fun orderNow(
        foodNames: MutableList<String>,
        foodPrices: MutableList<String>,
        foodQuantiles: MutableList<Int>,
        foodImage: MutableList<String>
    ) {
        // Gọi intent
        Log.d("CartFragment", "FoodItemName: $foodNames")
        Log.d("CartFragment", "FoodItemPrice: $foodPrices")
        Log.d("CartFragment", "FoodItemQuantiles: $foodQuantiles")
        Log.d("CartFragment", "FoodItemImages: $foodImage")
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutAcitvity::class.java)
            intent.putExtra("FoodItemName", foodNames as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrices as ArrayList<String>)
            intent.putExtra("FoodItemQuantiles", foodQuantiles as ArrayList<Int>)
            intent.putExtra("FoodItemImages", foodImage as ArrayList<Int>)
            startActivity(intent)
        }
    }

    // load gio hang duoi CSDL
    private fun retrieveCartItems() {
        // database reference to the Firease
        database = FirebaseDatabase.getInstance()
        // lay uid tai khoan dang nhao
        customerId = auth.currentUser?.uid ?: ""

        // khoi tao bien luu gia tri truy xuat
        val foodRef: DatabaseReference =
            database.reference.child("customer").child(customerId).child("CartItems")

        // list to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()
        typeOfDish = mutableListOf()

        // fetch data from the database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    // get the cartItems object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    // add cart items details to the list
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodImage?.let { foodImagesUri.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                    cartItems?.typeOfDish?.let { typeOfDish.add(it) }
                }
                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodDescriptions,
                    foodImagesUri,
                    quantity,
                    foodIngredients,
                    typeOfDish
                )
                binding.recyclerViewCardFood.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerViewCardFood.adapter = cartAdapter

                // Update total amount after cartAdapter is initialized
                totalAmount = cartAdapter.updateTotalPrice().toString()
                binding.totalPrice.text = formatPrice(totalAmount)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Không nhận được dữ liệu giỏ hàng", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }

    companion object {

    }
}