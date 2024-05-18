package com.example.foodapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodDescription: String? = null
    private var foodImage: String? = null
    private var foodIngredient: String? = null
    private var category: String? = null
    private var typeOfDish: String? = null
    private lateinit var commentAdapter: CommentAdapter
    private var listOfCommentItem: MutableList<Comment> = mutableListOf()
    private var comment: String? = null
    private var valueDiscount: String? = null

    private lateinit var relatedProductAdapter: RelatedProductAdapter
    private var listOfRelatedProducts: MutableList<MenuItem> = mutableListOf()


    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")
        typeOfDish = intent.getStringExtra("MenuTypeOfDish")
        category = intent.getStringExtra("MenuItemCategory")
        valueDiscount = intent.getStringExtra("MenuItemDiscount")

        // Nhận giá trị của category từ Intent
        category = intent.getStringExtra("MenuItemCategory")

        Log.d("TypeOfDish", "Type Of Dish received in DetailsActivity: $typeOfDish")
        Log.d("FoodName", "Category received in DetailsActivity: $foodName")
        Log.d("ValueDiscount", "Discount received in DetailsActivity: $valueDiscount")


        setUpComments(foodName)
        with(binding) {
            detailsFoodName.text = foodName
            detailsDescription.text = foodDescription
            detailsIngredient.text = foodIngredient
            detailsPrice.text = formatPrice(foodPrice)
            titleCategory.text = category
            titleTypeOfDish.text = typeOfDish
            detailsValueDiscount.text = valueDiscount

            // kiem tra xem anh not null thi hien anh con khong thi hien anh default food
            if (foodImage.isNullOrEmpty()) {
                Glide.with(this@DetailsActivity).load(R.drawable.default_food)
                    .into(detailsImageFood)

            } else {
                Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailsImageFood)
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
            val commentTitle = "Test"
            val foodComment = foodName
            comment = binding.editTextComment.text.toString()

            postComment(comment!!, starRating, commentTitle, foodComment as String)
//            Log.d("Value comment", "Comment : ${starRating}, ${commentTitle}, ${foodComment}")

        }
    }

    // Upload related products
    private fun loadRelatedProducts(typeOfDish: String) {
        val database = FirebaseDatabase.getInstance().reference
        val relatedProductsRef =
            database.child("menu").orderByChild("typeOfDishId").equalTo(typeOfDish)

        relatedProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    listOfRelatedProducts.clear()
                    snapshot.children.forEach { productSnapshot ->
                        productSnapshot.getValue(MenuItem::class.java)?.let {
                            if (it.foodName != foodName) {
                                listOfRelatedProducts.add(it)
                            }
                        }
                    }
//                    Log.d(
//                        "DetailsActivity",
//                        "Related products loaded: ${listOfRelatedProducts.size}"
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
        recyclerView.layoutManager = LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
        relatedProductAdapter = RelatedProductAdapter(listOfRelatedProducts)
        recyclerView.adapter = relatedProductAdapter
//        binding.recyclerViewRelatedProducts.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        relatedProductAdapter = RelatedProductAdapter(listOfRelatedProducts)
//        binding.recyclerViewRelatedProducts.adapter = relatedProductAdapter
    }


    // load comment
    private fun setUpComments(foodName: String?) {
        val database = FirebaseDatabase.getInstance().reference
        val loadCommentRef =
            database.child("comments").orderByChild("foodComment").equalTo(foodName)

        loadCommentRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Kiểm tra xem có dữ liệu bình luận không
                if (snapshot.exists()) {
                    // Dữ liệu bình luận tồn tại, hiển thị RecyclerView và titleComments
                    binding.recyclerViewComments.visibility = View.VISIBLE
                    binding.titleComments.visibility = View.VISIBLE
                    binding.divider3.visibility = View.VISIBLE

                    try {
                        // Xử lý dữ liệu bình luận
                        for (commentSnapshot in snapshot.children) {
                            val commentItem = commentSnapshot.getValue(Comment::class.java)
                            commentItem?.let { listOfCommentItem.add(it) }
                        }

//                        Log.d("Comment", "LIST OF COMMENT :   ${listOfCommentItem.size}")
                    } catch (e: DatabaseException) {
                        Log.e("FirebaseData", "Failed to convert item: ${e.message}")
                    }
                    // Thiết lập adapter cho RecyclerView
                    setCommentAdapter()
                } else {
                    // Không có dữ liệu bình luận, ẩn RecyclerView và titleComments
                    binding.recyclerViewComments.visibility = View.GONE
                    binding.titleComments.visibility = View.GONE
                    binding.divider3.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // setup to adapter
    private fun setCommentAdapter() {
        val commentName = mutableListOf<String>()
        val commentRating = mutableListOf<String>()
        val commentContent = mutableListOf<String>()

        for (i in 1 until listOfCommentItem.size) {
            listOfCommentItem[i].customerId.firstOrNull()?.let { commentName.add(it.toString()) }
            listOfCommentItem[i].star.let { commentRating.add(it.toString()) }
            listOfCommentItem[i].comment.let { commentContent.add(it) }
        }

        for (i in commentName.indices) {
            Log.d(
                "Comment",
                "customerID: ${commentName[i]}, Rating: ${commentRating[i]}, Content: ${commentContent[i]}"
            )
        }

        val recyclerView = binding.recyclerViewComments
        recyclerView.layoutManager = LinearLayoutManager(this@DetailsActivity)
        commentAdapter = CommentAdapter(
            listOfCommentItem
        )

        recyclerView.adapter = commentAdapter
    }


    // fun comment
    private fun postComment(
        commentContent: String,
        starRating: Float,
        commentTitle: String,
        foodComment: String,
    ) {
        val database = FirebaseDatabase.getInstance().reference
        val customerId = auth.currentUser?.uid ?: ""

        val comment = Comment(
            commentContent,
            System.currentTimeMillis(),
            customerId,
            starRating,
            commentTitle,
            foodComment,
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

    // fun add item to cart
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
            foodIngredient.toString(),
            typeOfDish.toString(),
            category.toString()
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