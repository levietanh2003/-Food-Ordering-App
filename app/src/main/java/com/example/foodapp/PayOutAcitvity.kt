package com.example.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.foodapp.databinding.ActivityPayOutAcitvityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class PayOutAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutAcitvityBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var name : String
    private lateinit var address: String
    private lateinit var phone : String
    private lateinit var totalAmount: String
    private lateinit var foodItemName : ArrayList<String>
    private lateinit var foodItemPrice : ArrayList<String>
    private lateinit var foodItemImage : ArrayList<String>
    private lateinit var foodItem : ArrayList<String>
    private lateinit var foodItemDescription : ArrayList<String>
    private lateinit var foodItemIngredient : ArrayList<String>
    private lateinit var foodItemQuantiles  : ArrayList<Int>
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        // set customer data
        setUpdate()

        // get customer details form Firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemQuantiles = intent.getIntegerArrayListExtra("FoodItemQuantiles") as ArrayList<Int>

//        foodItemName = intent.getStringArrayListExtra("FoodItemName") ?: ArrayList()
//        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") ?: ArrayList()
//        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") ?: ArrayList()
//        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") ?: ArrayList()
//        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") ?: ArrayList()
//        foodItemQuantiles = intent.getIntegerArrayListExtra("FoodItemQuantiles") ?: ArrayList()

        // Log các giá trị để kiểm tra
        Log.d("PayOutActivity", "foodItemName: $foodItemName")
        Log.d("PayOutActivity", "foodItemPrice: $foodItemPrice")
        Log.d("PayOutActivity", "foodItemQuantiles: $foodItemQuantiles")

        totalAmount = formatPrice(calculateTotalAmount().toString())

        //binding.payoutTotalAmount.isEnabled = false
        binding.payoutTotalAmount.text = totalAmount

        binding.btnPlaceMyOrder.setOnClickListener {
            val bottomSheetDeialog = CongratsBottomSheet()
            bottomSheetDeialog.show(supportFragmentManager,"Test")
        }

        // su kien tro ve
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for(i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastChar = price.last()
            val priceIntValue = if (lastChar == 'đ'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = foodItemQuantiles[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUpdate() {
        val customer = auth.currentUser
        if(customer != null){
            val customerId = customer.uid
            val customerRef = databaseReference.child("customer").child(customerId)

            customerRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names = snapshot.child("nameCustomer").getValue(String::class.java)?:""
                        val addresss = snapshot.child("addressCustomer").getValue(String::class.java)?:""
                        val phones = snapshot.child("phoneNumberCustomer").getValue(String::class.java)?:""
                        binding.apply {
                            payOutName.setText(names)
                            payOutAddress.setText(addresss)
                            payOutPhone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
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

}