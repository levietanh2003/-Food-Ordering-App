package com.example.foodapp

import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.foodapp.Model.CartItems
import com.example.foodapp.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var foodName : String ?= null
    private var foodPrice : String ?= null
    private var foodDescription : String ?= null
    private var foodImage : String ?= null
    private var foodIngredient : String ?= null

    private var typeOfDish : String ?= null
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        typeOfDish = intent.getStringExtra("MenuTypeOfDish")
//        binding.detailsFoodName.text = foodName
//        binding.detailsImageFood.setImageResource(foodImage)

        with(binding){
            detailsFoodName.text = foodName
            detailsDescription.text = foodDescription
            detailsIngredient.text = foodIngredient
            detailsPrice.text = formatPrice(foodPrice)
            titleTypeOfDish.text = typeOfDish
            // kiem tra xem anh not null thi hien anh con khong thi hien anh default food
            if(foodImage.isNullOrEmpty()){
                Glide.with(this@DetailsActivity).load(R.drawable.default_food).into(detailsImageFood)

            }else{
                Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailsImageFood)
            }
        }
        // xy ly su kien back
        binding.btnBack.setOnClickListener {
            finish()
        }

        // su ly su kien them vao gio hang
        binding.btnAddItemToCart.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""
        Log.d("CustomerId", customerId)


        // create cartItems object
        val cartItems = CartItems(
            foodName.toString(),
            foodPrice.toString(),
            foodDescription.toString(),
            foodImage.toString(),
            1,
            typeOfDish.toString()
        )

        // Query to check if the item already exists in the cart
        val cartQuery = database.child("customer").child(customerId).child("CartItems")
            .orderByChild("foodName").equalTo(foodName.toString())

        cartQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Item already exists in the cart, update its quantity
                    for (itemSnapshot in dataSnapshot.children) {
                        val existingItem = itemSnapshot.getValue(CartItems::class.java)
                        existingItem?.let {
                            val newQuantity = (it.foodQuantity ?: 0) + 1
                            itemSnapshot.ref.child("quantity").setValue(newQuantity)
                        }
                    }
                    Toast.makeText(
                        applicationContext,
                        "Sản phẩm đã được thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Item doesn't exist in the cart, add it
                    database.child("customer").child(customerId).child("CartItems").push()
                        .setValue(cartItems)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                "Thêm giỏ hàng thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Thêm giỏ hàng thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Cart query canceled.", databaseError.toException())
            }
        })
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