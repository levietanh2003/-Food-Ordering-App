package com.example.foodapp.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.DetailsActivity
import com.example.foodapp.Fragment.AllOrderFragment
import com.example.foodapp.Help.formatTimestamp
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

    inner class OrderViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // khoi tao tuy du lieu khi click vao item order
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailOrder(position)
                }
            }
        }

        private fun openDetailOrder(position: Int) {

            val orderIds = orderId[position]
            // thao tac chuyen puxStra Intent
//            val intentOrderDetails = Intent(context, AllOrderFragment::class.java).apply {
//                putExtra("OrderDetailsId", orderIds)
//            }
//            context.startActivity(intentOrderDetails)
            val bundle = bundleOf("OrderDetailsId" to orderIds)
            binding.root.findNavController()
                .navigate(R.id.action_orderFragment_to_allOrderFragment, bundle)
//
//            // kiem tra du lieu
//            Log.d("OrderDetailsID", "OrderDetailsID : $orderIds")
//            val activity = context as FragmentActivity
//            val fragment = AllOrderFragment().apply {
//                arguments = bundleOf("OrderDetailsId" to orderIds)
//            }
//
//            activity.supportFragmentManager.beginTransaction()
//                .replace(R.id.tvOrderId, fragment) // Ensure you have a container in your activity layout
//                .addToBackStack(null) // Optional: add this transaction to the back stack
//                .commit()

            // Log the orderId
            Log.d("OrderDetailsID", "OrderDetailsID : $orderIds")
        }


        fun bind(orderId: String, createdAt: String, deliveryStatus: String) {
            // Bind data to views
            binding.tvOrderId.text = orderId
            binding.tvOrderDate.text = formatTimestamp(createdAt.toLong())
            // Set color for the background based on delivery status
            val colorResId = getImageResourceIdForDeliveryStatus(deliveryStatus)
            binding.imageOrderState.setImageResource(colorResId)
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
