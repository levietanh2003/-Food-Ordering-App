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
import androidx.appcompat.view.menu.MenuAdapter
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.Adapter.CommentAdapter
import com.example.foodapp.Adapter.RelatedProductAdapter
import com.example.foodapp.Fragment.CartFragment
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
    private var foodId: String? = null

    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private var listOfRelatedProducts: MutableList<MenuItem> = mutableListOf()
    private var countDownTimer: CountDownTimer? = null

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Get the value of the word Intent
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

//        Log.d("FoodId", "FoodId received in DetailsActivity: $foodId")
//        Log.d("FoodName", "Category received in DetailsActivity: $foodName")
//        Log.d("ValueDiscount", "Discount received in DetailsActivity: $valueDiscount")
//        Log.d("CreatedAt", "Created At received in DetailsActivity: $createdAt")
//        Log.d("End At", "End At received in DetailsActivity: $endAt")

        // setUp Spinner titleComment
        setupTitleCommentSpinner()

        // setUp Comment
        setUpComments(foodId)

        with(binding) {
            detailsFoodName.text = foodName
            detailsDescription.text = foodDescription
            detailsIngredient.text = foodIngredient
            detailsPrice.text = formatPrice(foodPrice)
            titleCategory.text = category
            titleTypeOfDish.text = typeOfDish
            // check discount value
            if (valueDiscount != null) {
                titleDiscount.visibility = View.VISIBLE
                detailsValueDiscount.text = "${valueDiscount}% off"
            } else {
                titleDiscount.visibility = View.GONE
                detailsValueDiscount.visibility = View.GONE
            }

            // Check to see if you are not null. Show your child. Do not show your default food
            if (foodImage.isNullOrEmpty()) {
                Glide.with(this@DetailsActivity).load(R.drawable.default_food)
                    .into(detailsImageFood)

            } else {
                Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailsImageFood)
            }

            // Calculate the duration of the promotion program
            if (createdAt != null && endAt != null) {
                val remainingTime =
                    PromotionUtils.getRemainingTime(createdAt, endAt)
                if (remainingTime > 0) {
                    detailsPromotionEnd.visibility = View.VISIBLE
                    startCountDown(remainingTime)
                }
            } else {
                detailsPromotionEnd.visibility = View.GONE
            }

        }
        // xy ly su kien back
        binding.btnBack.setOnClickListener {
            finish()
        }

        // load related products
        typeOfDish?.let { loadRelatedProducts(it) }

        // su ly su kien them vao gio hang
        binding.btnAddItemToCart.setOnClickListener {
            addItemToCart()
        }
        binding.btnComment.setOnClickListener {
            val starRating = binding.ratingBar.rating
            // save selected customer in title comment
            val spinnerTitleComment = findViewById<Spinner>(R.id.spinnerTitleComment)
            val commentStatus = saveTitleComment(spinnerTitleComment)
            comment = binding.editTextComment.text.toString()

            postComment(comment!!, starRating, commentStatus)
//            Log.d("Value comment", "Comment : ${starRating}, ${commentTitle}, ${foodComment}")

        }

    }


    private fun startCountDown(remainingTime: Long) {
        countDownTimer?.cancel() // Cancel any existing timer

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

    // Upload related products
    private fun loadRelatedProducts(typeOfDish: String) {
        val database = FirebaseDatabase.getInstance().reference
        val relatedProductsRef =
            database.child("menu").orderByChild("typeOfDishId").equalTo(typeOfDish)

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
                                // discount
                                val discountValue =
                                    productSnapshot.child("discount").getValue(String::class.java)
                                if (discountValue != null) {
                                    it.discountValue = discountValue
                                }
                                // created At
                                val createdAt =
                                    productSnapshot.child("createAt").getValue(String::class.java)
                                if (createdAt != null) {
                                    it.createdAt = createdAt
                                }
                                // end At
                                val endAt =
                                    productSnapshot.child("endAt").getValue(String::class.java)
                                if (endAt != null) {
                                    it.endAt = endAt
                                }
                                // inStock
                                val inStock =
                                    productSnapshot.child("inStock").getValue(Boolean::class.java)
                                if (inStock == true) {
                                    filteredItems.add(it)
                                }
                            }
                        }
                    }
                    listOfRelatedProducts = filteredItems
//                    Log.d(
//                        "DetailsActivity",
//                        "Related products loaded: $listOfRelatedProducts"
//                    )
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
        recyclerView.layoutManager =
            LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
        relatedProductAdapter = RelatedProductAdapter(listOfRelatedProducts)
        recyclerView.adapter = relatedProductAdapter
    }

    private fun setupTitleCommentSpinner() {
        // Initialize the Spinner and the list of titles
        val spinnerTitleCommentMethod: Spinner = binding.spinnerTitleComment
        val titleSpinner = arrayOf("Very Good", "Good", "Bad", "Very Bad")

        // Create Adapter and set it for the Spinner
        val titleCommentAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, titleSpinner)
        titleCommentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTitleCommentMethod.adapter = titleCommentAdapter
    }


    // load comment
    private fun setUpComments(foodId: String?) {

        val database = FirebaseDatabase.getInstance().reference

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

                                // Lấy tên khách hàng tương ứng và thêm vào danh sách
                                val customerId = it.customerID
                                val nameRef = database.child("customer").child(customerId)
                                nameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(nameSnapshot: DataSnapshot) {
                                        val customerName = nameSnapshot.child("nameCustomer")
                                            .getValue(String::class.java)
                                        customerName?.let {
                                            listOfCustomerNames.add(it)
                                        }
                                        // Kiểm tra xem danh sách tên khách hàng đã hoàn thành chưa
                                        if (listOfCustomerNames.size == listOfCommentItem.size) {
                                            // Tất cả các tên khách hàng đã được lấy, tiến hành hiển thị
                                            setCommentAdapter(
                                                listOfCommentItem,
                                                listOfCustomerNames
                                            )
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e(
                                            "DetailsActivity",
                                            "Failed to fetch customer name: ${error.message}"
                                        )
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

    private fun postComment(
        comment: String,
        starRating: Float,
        commentStatus: String,
    ) {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""
        // Reference to the user's node
        val comment = Comment(
            foodId,
            comment,
            System.currentTimeMillis(),
            customerId,
            starRating,
            commentStatus,
        )
        val commentsRef = database.child("comments").push()
        commentsRef.setValue(comment)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Bình luận đã được đăng", Toast.LENGTH_SHORT)
                    .show()
                // Xóa nội dung nhập sau khi bình luận được đăng
                binding.editTextComment.setText("")
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun saveTitleComment(spinner: Spinner): String {
        val commentStatus = spinner.selectedItem.toString()
        return commentStatus
    }

    // selected title comment

    // fun add item to cart
    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""
        Log.d("CustomerId", customerId)


        // create cartItems object
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
                            itemSnapshot.ref.child("foodQuantity").setValue(newQuantity)
                        }
                    }
                    Toast.makeText(
                        applicationContext,
                        "The product has been added to cart",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Item doesn't exist in the cart, add it
                    database.child("customer").child(customerId).child("CartItems").push()
                        .setValue(cartItems)
                        .addOnSuccessListener {
                            Toast.makeText(
                                applicationContext,
                                "The product has been added to cart",
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