package com.example.foodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.databinding.OrderItemBinding

class OrderAdapter(
    private val orderId: MutableList<String>,
    private val createAt: MutableList<String>,
    private val deliveryStatus: MutableList<String>,
    private val context: Context
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = OrderItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        // Bind data to views inside the ViewHolder
        holder.bind(orderId[position], createAt[position], deliveryStatus[position])
    }

    override fun getItemCount(): Int = orderId.size

    inner class OrderViewHolder(private val binding: OrderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(orderId: String, createdAt: String, deliveryStatus: String) {
            // Bind data to views
            binding.tvOrderId.text = orderId
            binding.tvOrderDate.text = createdAt
            // Set color for the background based on delivery status
            val colorResId = getImageResourceIdForDeliveryStatus(deliveryStatus)
            binding.root.setBackgroundColor(ContextCompat.getColor(context, colorResId))
        }

        private fun getImageResourceIdForDeliveryStatus(deliveryStatus: String): Int {
            return when (deliveryStatus) {
                "Pending" -> R.color.g_orange_yellow
                "Delivering" -> R.color.g_green
                "Done" -> R.color.g_green
                "Cancel" -> R.color.g_red
                else -> R.color.black // Nếu không khớp với bất kỳ trạng thái nào khác
            }
        }

    }
}
