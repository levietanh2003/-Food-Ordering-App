package com.example.foodapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.NotifactionItemBinding

class NotificationAdapter(private val NotificationText : ArrayList<String>, private val NotificationImage : ArrayList<Int>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotifactionItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = NotificationText.size

    inner class NotificationViewHolder(private val binding: NotifactionItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            binding.apply {
                notificationText.text = NotificationText[position]
                notificationImage.setImageResource(NotificationImage[position])
            }
        }
    }
}