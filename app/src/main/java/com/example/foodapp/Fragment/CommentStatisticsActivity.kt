package com.example.foodapp.Fragment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.foodapp.Model.Comment
import com.example.foodapp.databinding.ActivityCommentStatisticsFragmentBinding
import com.google.firebase.database.*

class CommentStatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentStatisticsFragmentBinding

    //    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var foodId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentStatisticsFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Thiết lập cứng số sao cho mỗi RatingBar
        binding.ratingBarFiveStar.rating = 5.0f
        binding.ratingBarFourStar.rating = 4.0f
        binding.ratingBarThreeStar.rating = 3.0f
        binding.ratingBarTwoStar.rating = 2.0f
        binding.ratingBarOneStar.rating = 1.0f

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        // Retrieve the foodId from the Intent
        foodId = intent.getStringExtra("foodId")

        foodId?.let { fetchCommentStatistics(it) }
    }

    private fun fetchCommentStatistics(foodId: String) {
        // Access the BuyHistory node for the customer
        val commentStatisticsRef: DatabaseReference =
            database.reference.child("comments")

//        val x = commentStatisticsRef.child("productID").child(foodId)

        commentStatisticsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Khởi tạo các biến đếm cho mỗi mức đánh giá
                var countFiveStar = 0
                var countFourStar = 0
                var countThreeStar = 0
                var countTwoStar = 0
                var countOneStar = 0

                // Lặp qua tất cả các comment
                for (commentSnapshot in snapshot.children) {
                    val comment = commentSnapshot.getValue(Comment::class.java)

                    // Kiểm tra nếu comment thuộc về sản phẩm foodId
                    if (comment?.productID == foodId) {
                        // Tăng biến đếm tương ứng với số sao của comment
                        when (comment.star) {
                            5.0f -> countFiveStar++
                            4.0f -> countFourStar++
                            3.0f -> countThreeStar++
                            2.0f -> countTwoStar++
                            1.0f -> countOneStar++
                        }
                    }
                }

                Log.d("5*", "5* : $countFiveStar")
                Log.d("4*", "4* : $countFourStar")
                Log.d("3*", "3* : $countThreeStar")
                Log.d("2*", "2* : $countTwoStar")
                Log.d("1*", "1* : $countOneStar")


                // Hiển thị số lượng từng sao lên TextView tương ứng
                binding.textViewCountFiveStar.text = " ($countFiveStar)"
                binding.textViewCountFourStar.text = " ($countFourStar)"
                binding.textViewCountThreeStar.text = " ($countThreeStar)"
                binding.textViewCountTwoStar.text = " ($countTwoStar)"
                binding.textViewCountOneStar.text = " ($countOneStar)"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}