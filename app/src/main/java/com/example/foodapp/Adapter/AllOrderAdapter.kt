package com.example.foodapp.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.databinding.RvProductItemBinding

class AllOrderAdapter(
    private val allOrders: List<OrderDetails>,
    private val requireContext: Context
) : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllOrderViewHolder {
        val binding = RvProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allOrders.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AllOrderViewHolder(private val binding: RvProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val x  = allOrders[position]
            binding.apply {
                tvProductCartName.text = x.foodNames.toString()
                tvProductCartPrice.text = x.foodPrices.toString()
                tvBillingProductQuantity.text =  x.foodQuantities.toString()
                val uri = Uri.parse(x.foodImage.toString())
                Glide.with(requireContext).load(uri).into(imageCartProduct)
            }
        }
    }
}
