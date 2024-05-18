package com.example.foodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.Help.formatTimestamp
import com.example.foodapp.Model.Comment
import com.example.foodapp.databinding.CommentItemBinding


class CommentAdapter(
    private val comments : List<Comment>
): RecyclerView.Adapter<CommentAdapter.CommentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CommentsViewHolder(binding)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class CommentsViewHolder(private val binding: CommentItemBinding) :  RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int){
            val itemComment = comments[position]
            binding.apply {
                textCommentName.text = itemComment.customerId
                textCommentContent.text = itemComment.comment
                ratingBarComment.rating = itemComment.star
                textCommentCreatedAt.text = formatTimestamp(itemComment.createdAt)
            }
        }
    }
}