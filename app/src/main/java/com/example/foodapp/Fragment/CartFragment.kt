package com.example.foodapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.CartAdapter
import com.example.foodapp.Model.CartItems
import com.example.foodapp.PayOutAcitvity
import com.example.foodapp.databinding.ActivityCartFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
    private lateinit var foodDiscounts: MutableList<String>
    private lateinit var foodPricePayOut: String

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

            var result = "0VND"

            if (binding.totalPrice.text == result) {
                Toast.makeText(
                    requireContext(),
                    "There are no products to pay for",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // get order items details before proceeding to check out
                getItemOrderDetail()
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(com.example.foodapp.R.id.action_cartFragment_to_homeFragment)
        }

    }

    private fun getItemOrderDetail() {
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
        foodImage: MutableList<String>,
    ) {
        // Gọi intent

        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutAcitvity::class.java)
            intent.putExtra("FoodItemName", foodNames as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrices as ArrayList<String>)
            intent.putExtra("FoodItemQuantiles", foodQuantiles as ArrayList<Int>)
            intent.putExtra("FoodItemImages", foodImage as ArrayList<Int>)
            intent.putExtra("FoodItemTotalPrice", foodPricePayOut)

            startActivity(intent)
        }
//        Log.d("CartFragment", "FoodItemName: $foodNames")
//        Log.d("CartFragment", "FoodItemPrice: $foodPrices")
//        Log.d("CartFragment", "FoodItemQuantiles: $foodQuantiles")
        Log.d("CartFragment", "FoodItemTotalPrice: $foodPricePayOut")
    }

    // load gio hang duoi CSDL
    private fun retrieveCartItems() {
        showProgressBarCart()
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
        foodDiscounts = mutableListOf()
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
                    cartItems?.foodDiscount?.let { foodDiscounts.add(it) }

                }
                setAdapter()
                hideProgressBarCart()
            }

            private fun setAdapter() {
                val totalPrice = binding.totalPrice
                cartAdapter = CartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodDescriptions,
                    foodImagesUri,
                    quantity,
                    foodIngredients,
                    typeOfDish,
                    foodDiscounts,
                    totalPrice
                )

                binding.recyclerViewCardFood.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.recyclerViewCardFood.adapter = cartAdapter

                foodPricePayOut = cartAdapter.getTotalPrice()
                Log.d("price", "price: $foodPricePayOut")

                // Check if cart is empty and show default image
                if (cartAdapter.itemCount == 0) {
                    binding.emptyCartImage.visibility = View.VISIBLE
                } else {
                    binding.emptyCartImage.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Không nhận được dữ liệu giỏ hàng", Toast.LENGTH_SHORT)
                    .show()

            }
        })
    }

    // Hiện ProgressBar
    private fun showProgressBarCart() {
        binding.progressCart.visibility = View.VISIBLE
    }

    // Ẩn ProgressBar
    private fun hideProgressBarCart() {
        binding.progressCart.visibility = View.GONE
    }

    companion object {

    }
}