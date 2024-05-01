package com.example.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.databinding.ActivityPayOutAcitvityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
    private lateinit var foodItemQuantiles  : ArrayList<String>
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        setUpdate()

        binding.btnPlaceMyOrder.setOnClickListener {
            val bottomSheetDeialog = CongratsBottomSheet()
            bottomSheetDeialog.show(supportFragmentManager,"Test")
        }
    }

    private fun setUpdate() {
        val customer = auth.currentUser
        if(customer != null){
            val customerId = customer.uid
            val customerRef = databaseReference.child("customer").child(customerId)

            customerRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val addresss = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            payOutName.setText(names)
                            payOutAddress.setText(addresss)
                            payOutPhone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

}