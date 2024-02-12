package com.example.foodapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.BuyAgainItemBinding


class BuyAgainAdapter(private val BuyAgainNameFood : ArrayList<String>, private val BuyAgainPrice : ArrayList<String>, private val BuyAgainImage : ArrayList<Int>) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {
holder.bind(BuyAgainNameFood[position],BuyAgainPrice[position],BuyAgainImage[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyAgainViewHolder(binding)
    }

    override fun getItemCount(): Int = BuyAgainNameFood.size

    class BuyAgainViewHolder(private val binding: BuyAgainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(buyAgainNameFood: String, buyAgainPrice: String, buyAgainImage: Int) {
            binding.buyAgainFoodName.text = buyAgainNameFood
            binding.buyAgainPrice.text = buyAgainPrice
            binding.buyAgainImage.setImageResource(buyAgainImage)
        }
    }
    companion object{

    }

}