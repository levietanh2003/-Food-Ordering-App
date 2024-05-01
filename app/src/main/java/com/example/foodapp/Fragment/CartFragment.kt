package com.example.foodapp.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Adapter.CartAdapter
import com.example.foodapp.CongratsBottomSheet
import com.example.foodapp.Model.CartItems
import com.example.foodapp.PayOutAcitvity
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityCartFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CartFragment : Fragment() {
    private lateinit var binding: ActivityCartFragmentBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var foodNames : MutableList<String>
    private lateinit var foodPrices : MutableList<String>
    private lateinit var foodDescriptions : MutableList<String>
    private lateinit var foodImagesUri : MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var quantity : MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var customerId : String
    private lateinit var typeOfDishs : MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View ? {
        binding = ActivityCartFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        reteriveCartItems()

        // xu ly nut thanh toan
        binding.btnProceed.setOnClickListener {
            val intent = Intent(requireContext(), PayOutAcitvity::class.java)
            startActivity(intent)
        }
    }

    private fun reteriveCartItems() {
        // database reference to the Firease
        database = FirebaseDatabase.getInstance()
        // lay uid tai khoan dang nhao
        customerId = auth.currentUser?.uid?:""

        // khoi tao bien luu gia tri truy xuat
        val foodRef : DatabaseReference = database.reference.child("customer").child(customerId).child("CartItems")

        // list to store cart items
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()
        typeOfDishs = mutableListOf()

        // fetch data from the database
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    // get the cartItems object from the child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    // add cart items details to the list
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodImage?.let { foodImagesUri.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                    cartItems?.typeOfDish?.let { typeOfDishs.add(it) }
                }
                setAdapter()
            }
            private fun setAdapter() {
                val adapter = CartAdapter(requireContext(),foodNames,foodPrices,foodDescriptions,foodImagesUri,quantity,foodIngredients,typeOfDishs)
                binding.recyclerViewCardFood.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerViewCardFood.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Không nhận được dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show()

            }
        })
    }

    companion object{

    }
}