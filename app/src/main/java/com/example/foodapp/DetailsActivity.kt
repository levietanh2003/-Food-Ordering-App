package com.example.foodapp

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
//        binding.detailsFoodName.text = foodName
//        binding.detailsImageFood.setImageResource(foodImage)

        with(binding){
            detailsFoodName.text = foodName
            detailsDescription.text = foodDescription
            detailsIngredient.text = foodIngredient
            detailsPrice.text = formatPrice(foodPrice)
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
        var database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid?:""

        // create cartItems object
        val cartItems = CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)

        // save data to cart item to firebase database
        database.child("customer").child(customerId).child("CartItems").push().setValue(cartItems).addOnSuccessListener {
            Toast.makeText(this,"Thêm giỏ hàng thành công",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this,"Thêm giỏ hàng thất bại",Toast.LENGTH_SHORT).show()

        }
    }
//    private fun addItemToCart() {
//        val database = FirebaseDatabase.getInstance().reference
//        val customerId = auth.currentUser?.uid ?: ""
//
//
//
//    // Tạo một đối tượng CartItem từ thông tin hiện tại
//        val cartItem = CartItems(
//            foodName.toString(),
//            foodPrice.toString(),
//            foodDescription.toString(),
//            foodImage.toString(),
//            1 // Số lượng ban đầu
//        )
//
//        // Tham chiếu đến giỏ hàng của khách hàng trong Firebase Database
//        val cartRef = database.child("customers").child(customerId).child("cartItems")
//        val newCartItemRef = cartRef.push()
//        newCartItemRef.setValue(cartItem.copy(cartItemId = newCartItemRef.key))
//        // Kiểm tra xem giỏ hàng của khách hàng đã tồn tại chưa
//        cartRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    // Giỏ hàng đã tồn tại
//                    val cartItemsList = snapshot.children.mapNotNull { it.getValue(CartItems::class.java) }
//                    var itemExists = false
//                    for (cartItemData in cartItemsList) {
//                        // Kiểm tra xem mặt hàng đã tồn tại trong giỏ hàng chưa
//                        if (cartItemData.foodName == foodName) {
//                            // Mặt hàng đã tồn tại trong giỏ hàng, tăng số lượng
//                            cartRef.child(cartItemData.cartItemId).child("quantity").setValue(cartItemData.quantity + 1)
//                            itemExists = true
//                            break
//                        }
//                    }
//                    if (!itemExists) {
//                        // Mặt hàng chưa tồn tại trong giỏ hàng, thêm mới vào giỏ hàng
//                        val newCartItemRef = cartRef.push()
//                        newCartItemRef.setValue(cartItem.copy(cartItemId = newCartItemRef.key))
//                    }
//                } else {
//                    // Giỏ hàng chưa tồn tại, tạo mới giỏ hàng và thêm mặt hàng vào đó
//                    val newCartItemRef = cartRef.push()
//                    newCartItemRef.setValue(cartItem.copy(cartItemId = newCartItemRef.key))
//                }
//                Toast.makeText(this@DetailsActivity, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(this@DetailsActivity, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

//    private fun addItemToCart() {
//        val database = FirebaseDatabase.getInstance().reference
//        val customerId = auth.currentUser?.uid ?:""
//
//        Log.d("CustomerId", customerId)
//        // Tạo một đối tượng CartItem từ thông tin hiện tại
//        val cartItem = CartItems(
//            foodName.toString(),
//            foodPrice.toString(),
//            foodDescription.toString(),
//            foodImage.toString(),
//            1 // Số lượng ban đầu
//        )
//
//        // Tham chiếu đến giỏ hàng của khách hàng trong Firebase Database
//        val cartRef = database.child("customer").child(customerId).child("CartItems")
//
//        // Thêm một mặt hàng mới vào giỏ hàng với một ID duy nhất
//        val newCartItemRef = cartRef.push()
//        newCartItemRef.setValue(cartItem)
//            .addOnSuccessListener {
//                Toast.makeText(this, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show()
//            }
//    }

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