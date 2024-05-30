package com.example.foodapp.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.Help.formatPrice
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.R
import com.example.foodapp.databinding.RvProductItemBinding

class AllOrderAdapter(
    private val allOrderNameFood: List<String>,
    private val allOrderPriceFood: List<String>,
    private val allOrderQuantities: List<Int>,
    private val allOrderImage: List<String>,
    private val requireContext: Context
) : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllOrderViewHolder {
        val binding =
            RvProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return allOrderNameFood.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AllOrderViewHolder(private val binding: RvProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                tvProductCartName.text = allOrderNameFood[position]
                tvBillingProductQuantity.text = allOrderQuantities[position].toString()
                tvProductCartPrice.text = formatPrice(allOrderPriceFood[position])
                val uri = Uri.parse(allOrderImage[position])
                Glide.with(requireContext)
                    .load(uri)
                    .placeholder(R.drawable.ic_show) // optional placeholder
                    .error(R.drawable.ic_history) // optional error image
                    .into(imageCartProduct)
            }
        }
    }
}
