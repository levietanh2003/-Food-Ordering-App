package com.example.foodapp.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.databinding.BuyAgainItemBinding
import java.text.NumberFormat
import java.util.*


class BuyAgainAdapter(
    private val BuyAgainNameFood: MutableList<String>,
    private val BuyAgainPrice: MutableList<String>,
    private val BuyAgainImage: MutableList<String>,
    private var requireContext : Context
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
        holder.bind(BuyAgainNameFood[position], BuyAgainPrice[position], BuyAgainImage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding =
            BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = BuyAgainNameFood.size

    private fun formatPrice(price: String?): String {
        return try {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            val parsedPrice = price?.toDouble() ?: 0.0
            numberFormat.format(parsedPrice)
        } catch (e: Exception) {
            "0 VNĐ" // Trả về mặc định nếu không thể định dạng giá
        }
    }

    inner class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buyAgainNameFood: String, buyAgainPrice: String, buyAgainImage: String) {
            binding.buyAgainFoodName.text = buyAgainNameFood
            binding.buyAgainPrice.text = formatPrice(buyAgainPrice)
            val uri = Uri.parse(buyAgainImage)
            Glide.with(requireContext).load(uri).into(binding.buyAgainImage)
        }
    }
}