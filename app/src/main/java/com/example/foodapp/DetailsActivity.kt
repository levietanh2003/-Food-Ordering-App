package com.example.foodapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.Adapter.CommentAdapter
import com.example.foodapp.Adapter.RelatedProductAdapter
import com.example.foodapp.Model.CartItems
import com.example.foodapp.Model.Comment
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private var countDownTimer: CountDownTimer? = null

    private var listOfRelatedProducts: MutableList<MenuItem> = mutableListOf()

    private var foodId: String? = null
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodImage: String? = null
    private var foodIngredient: String? = null
    private var category: String? = null
    private var typeOfDish: String? = null
    private var comment: String? = null
    private var valueDiscount: String? = null
    private var createdAt: String? = null
    private var endAt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Retrieve data from Intent
        retrieveIntentData()

        // Set up UI elements
        setupTitleCommentSpinner()
        setUpComments(foodId)
        setUpDetails()

        // Load related products
        typeOfDish?.let { loadRelatedProducts(it) }

        // Event listeners
        binding.btnBack.setOnClickListener { finish() }
        binding.btnAddItemToCart.setOnClickListener { addItemToCart() }
        binding.btnComment.setOnClickListener {
            val starRating = binding.ratingBar.rating
            val spinnerTitleComment = findViewById<Spinner>(R.id.spinnerTitleComment)
            val commentStatus = saveTitleComment(spinnerTitleComment)
            comment = binding.editTextComment.text.toString()

            postComment(comment!!, starRating, commentStatus)
        }
    }

    private fun retrieveIntentData() {
        foodId = intent.getStringExtra("MenuItemId")
        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")
        typeOfDish = intent.getStringExtra("MenuTypeOfDish")
        valueDiscount = intent.getStringExtra("MenuItemDiscount")
        category = intent.getStringExtra("MenuItemCategory")
        createdAt = intent.getStringExtra("MenuItemCreatedAt")
        endAt = intent.getStringExtra("MenuItemEndAt")
    }

    private fun setUpDetails() {
        with(binding) {
            detailsFoodName.text = foodName
            detailsDescription.text = foodDescription
            detailsIngredient.text = foodIngredient
            detailsPrice.text = formatPrice(foodPrice)
            titleCategory.text = category
            titleTypeOfDish.text = typeOfDish

            if (valueDiscount != null) {
                titleDiscount.visibility = View.VISIBLE
                detailsValueDiscount.text = "${valueDiscount}% off"
            } else {
                titleDiscount.visibility = View.GONE
                detailsValueDiscount.visibility = View.GONE
            }

            if (foodImage.isNullOrEmpty()) {
                Glide.with(this@DetailsActivity).load(R.drawable.default_food).into(detailsImageFood)
            } else {
                Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailsImageFood)
            }

            if (createdAt != null && endAt != null) {
                val remainingTime = PromotionUtils.getRemainingTime(createdAt, endAt)
                if (remainingTime > 0) {
                    detailsPromotionEnd.visibility = View.VISIBLE
                    startCountDown(remainingTime)
                }
            } else {
                detailsPromotionEnd.visibility = View.GONE
            }
        }
    }

    private fun setupTitleCommentSpinner() {
        val spinnerTitleComment: Spinner = binding.spinnerTitleComment
        val titleSpinner = arrayOf("Very Good", "Good", "Bad", "Very Bad")
        val titleCommentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titleSpinner)
        titleCommentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTitleComment.adapter = titleCommentAdapter
    }

    private fun setUpComments(foodId: String?) {
        val database = FirebaseDatabase.getInstance().reference
        showProgressBarCart()
        val loadCommentRef = database.child("comments")

        loadCommentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.recyclerViewComments.visibility = View.VISIBLE
                    binding.titleComments.visibility = View.VISIBLE
                    binding.divider3.visibility = View.VISIBLE

                    val listOfCommentItem: MutableList<Comment> = mutableListOf()
                    val listOfCustomerNames: MutableList<String> = mutableListOf()

                    snapshot.children.forEach { commentSnapshot ->
                        val commentItem = commentSnapshot.getValue(Comment::class.java)
                        commentItem?.let {
                            if (it.productID == foodId) {
                                listOfCommentItem.add(it)
                                val customerId = it.customerID
                                val nameRef = database.child("customer").child(customerId)
                                nameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(nameSnapshot: DataSnapshot) {
                                        val customerName = nameSnapshot.child("nameCustomer")
                                            .getValue(String::class.java)
                                        customerName?.let {
                                            listOfCustomerNames.add(it)
                                        }
                                        if (listOfCustomerNames.size == listOfCommentItem.size) {
                                            hideProgressBarCart()
                                            setCommentAdapter(listOfCommentItem, listOfCustomerNames)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("DetailsActivity", "Failed to fetch customer name: ${error.message}")
                                    }
                                })
                            }
                        }
                    }
                } else {
                    binding.recyclerViewComments.visibility = View.GONE
                    binding.titleComments.visibility = View.GONE
                    binding.divider3.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DetailsActivity", "Failed to fetch comments: ${error.message}")
            }
        })
    }

    private fun setCommentAdapter(comments: List<Comment>, customerNames: List<String>) {
        val recyclerView = binding.recyclerViewComments
        recyclerView.layoutManager = LinearLayoutManager(this@DetailsActivity)
        val commentAdapter = CommentAdapter(comments, customerNames)
        recyclerView.adapter = commentAdapter
    }

    private fun postComment(comment: String, starRating: Float, commentStatus: String) {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""
        val comment = Comment(foodId, comment, System.currentTimeMillis(), customerId, starRating, commentStatus)
        val commentsRef = database.child("comments").push()

        commentsRef.setValue(comment)
            .addOnSuccessListener {
                setUpComments(foodId)
                Toast.makeText(applicationContext, "Comment has been posted", Toast.LENGTH_SHORT).show()
                binding.ratingBar.rating = 0.0f
                binding.editTextComment.setText("")
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveTitleComment(spinner: Spinner): String {
        return spinner.selectedItem.toString()
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""
        val cartItems = CartItems(
            foodName,
            foodPrice,
            foodDescription,
            foodImage,
            1,
            foodIngredient,
            typeOfDish,
            category.toString(),
            valueDiscount
        )

        val cartQuery = database.child("customer").child(customerId).child("CartItems")
            .orderByChild("foodName").equalTo(foodName.toString())

        cartQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (itemSnapshot in dataSnapshot.children) {
                        val existingItem = itemSnapshot.getValue(CartItems::class.java)
                        existingItem?.let {
                            val newQuantity = (it.foodQuantity ?: 0) + 1
                            itemSnapshot.ref.child("foodQuantity").setValue(newQuantity)
                        }
                    }
                    Toast.makeText(applicationContext, "The product has been added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    database.child("customer").child(customerId).child("CartItems").push()
                        .setValue(cartItems)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "The product has been added to cart", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Cart query canceled.", databaseError.toException())
            }
        })
    }

    private fun loadRelatedProducts(typeOfDish: String) {
        val database = FirebaseDatabase.getInstance().reference
        val relatedProductsRef = database.child("menu").orderByChild("typeOfDishId").equalTo(typeOfDish)

        listOfRelatedProducts = mutableListOf()
        relatedProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredItems = mutableListOf<MenuItem>()
                if (snapshot.exists()) {
                    listOfRelatedProducts.clear()
                    snapshot.children.forEach { productSnapshot ->
                        productSnapshot.getValue(MenuItem::class.java)?.let {
                            if (it.foodName != foodName) {
                                it.foodId = productSnapshot.key
                                it.discountValue = productSnapshot.child("discount").getValue(String::class.java)
                                it.createdAt = productSnapshot.child("createAt").getValue(String::class.java)
                                it.endAt = productSnapshot.child("endAt").getValue(String::class.java)
                                if (productSnapshot.child("inStock").getValue(Boolean::class.java) == true) {
                                    filteredItems.add(it)
                                }
                            }
                        }
                    }
                    listOfRelatedProducts = filteredItems
                    setRelatedProductAdapter()
                } else {
                    Log.d("DetailsActivity", "No related products found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DetailsActivity", "Failed to load related products: ${error.message}")
            }
        })
    }

    private fun setRelatedProductAdapter() {
        val recyclerView = binding.recyclerViewRelatedProducts
        recyclerView.layoutManager = LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
        relatedProductAdapter = RelatedProductAdapter(listOfRelatedProducts)
        recyclerView.adapter = relatedProductAdapter
    }

    private fun startCountDown(remainingTime: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(remainingTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val days = millisUntilFinished / (1000 * 60 * 60 * 24)
                val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                val minutes = (millisUntilFinished / (1000 * 60)) % 60
                val seconds = (millisUntilFinished / 1000) % 60

                binding.detailsPromotionEnd.text = String.format(
                    Locale.getDefault(),
                    "Ends in %02d:%02d:%02d:%02d",
                    days, hours, minutes, seconds
                )
            }

            override fun onFinish() {
                binding.detailsPromotionEnd.text = "Promotion ended"
            }
        }.start()
    }

    private fun showProgressBarCart() {
        binding.progressComment.visibility = View.VISIBLE
    }

    private fun hideProgressBarCart() {
        binding.progressComment.visibility = View.GONE
    }

    private fun formatPrice(price: String?): String {
        return try {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            val parsedPrice = price?.toDouble() ?: 0.0
            numberFormat.format(parsedPrice)
        } catch (e: Exception) {
            "0 VNĐ"
        }
    }
}
