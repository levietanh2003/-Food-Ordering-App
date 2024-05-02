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
    private lateinit var typeOfDish : MutableList<String>
//    private lateinit var totalAmount: String


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
        retrieveCartItems()


        // xu ly nut thanh toan
        binding.btnProceed.setOnClickListener {
            // get order items details before proceeding to check out
            getItemOrderDetail()
//            val intent = Intent(requireContext(), PayOutAcitvity::class.java)
//            startActivity(intent)
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

        orderNow(foodNames, foodPrices, foodQuantiles)


//        val foodName = mutableListOf<String>()
//        val foodPrice = mutableListOf<String>()
//        val foodImage = mutableListOf<String>()
//        val foodDescription = mutableListOf<String>()
//        val foodIngredient = mutableListOf<String>()

        //get items Quantities


//        orderIdRef.addListenerForSingleValueEvent(object  : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // get the cartItems to respective List
//                for (foodSnapshot in snapshot.children){
//                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
//                    // add items details in to list
//                    orderItems?.foodName?.let { foodName.add(it) }
//                    orderItems?.foodPrice?.let { foodPrice.add(it) }
//                    orderItems?.foodDescription?.let { foodDescription.add(it) }
//                    orderItems?.foodImage?.let { foodImage.add(it) }
//                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
//                }
//                orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredient,foodQuantiles)
//            }

//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(),"Không nhận được dữ liệu", Toast.LENGTH_SHORT).show()
//            }
//            private fun orderNow(
//                foodName: MutableList<String>,
//                foodPrice: MutableList<String>,
//                foodDescription: MutableList<String>,
//                foodImage: MutableList<String>,
//                foodIngredient: MutableList<String>,
//                foodQuantiles: MutableList<Int>
//            ) {
//                // Log các giá trị trước khi khởi chạy intent
//                Log.d("CartFragment", "FoodItemName: $foodName")
//                Log.d("CartFragment", "FoodItemPrice: $foodPrice")
//                Log.d("CartFragment", "FoodItemDescription: $foodDescription")
//                Log.d("CartFragment", "FoodItemImage: $foodImage")
//                Log.d("CartFragment", "FoodItemIngredient: $foodIngredient")
//                Log.d("CartFragment", "FoodItemQuantiles: $foodQuantiles")
//
//                if (isAdded && context != null){
//                    val intent = Intent(requireContext(),PayOutAcitvity::class.java)
//                    intent.putExtra("FoodItemName", foodName as ArrayList<String>)
//                    intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
//                    intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
//                    intent.putExtra("FoodItemImage", foodImage as ArrayList<String>)
//                    intent.putExtra("FoodItemIngredient", foodIngredient as ArrayList<String>)
//                    intent.putExtra("FoodItemQuantiles", foodQuantiles as ArrayList<Int>)
//
//                    startActivity(intent)
//                }
//            }
//        })
    }
    private fun orderNow(
        foodNames: MutableList<String>,
        foodPrices: MutableList<String>,
        foodQuantiles: MutableList<Int>
    ) {
        // Gọi intent
        Log.d("CartFragment", "FoodItemName: $foodNames")
        Log.d("CartFragment", "FoodItemPrice: $foodPrices")
        Log.d("CartFragment", "FoodItemQuantiles: $foodQuantiles")
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutAcitvity::class.java)
            intent.putExtra("FoodItemName", foodNames as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrices as ArrayList<String>)
            intent.putExtra("FoodItemQuantiles", foodQuantiles as ArrayList<Int>)
            startActivity(intent)
        }
    }

// load gio hang duoi CSDL
    private fun retrieveCartItems() {
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
        typeOfDish = mutableListOf()

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
                    cartItems?.typeOfDish?.let { typeOfDish.add(it) }
                }
                setAdapter()
            }
            private fun setAdapter() {
                cartAdapter = CartAdapter(requireContext(),foodNames,foodPrices,foodDescriptions,foodImagesUri,quantity,foodIngredients,typeOfDish)
                binding.recyclerViewCardFood.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.recyclerViewCardFood.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Không nhận được dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show()

            }
        })
    }

    companion object{

    }
}