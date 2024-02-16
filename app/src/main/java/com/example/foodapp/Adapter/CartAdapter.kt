package com.example.foodapp.Adapter

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.DetailsActivity
import com.example.foodapp.databinding.CartItemBinding

class CartAdapter(private val CartItems: MutableList<String>, private val CartItemPrice : MutableList<String>,private val CartImage: MutableList<Int>, private val requireContext: Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


private val itemQuantities = IntArray(CartItems.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(position)

        holder.itemView.setOnClickListener {
            // luong xu ly intent anh
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("FilteredMenuItems", CartItems[position])
            intent.putExtra("FilteredMenuItemImage", CartImage[position])
            requireContext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
       return  CartItems.size
    }
    inner class CartViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int){
            binding.apply {
                    val quanlity = itemQuantities[position]
                cartFoodName.text = CartItems[position]
                cartItemPrice.text = CartItemPrice[position]
                cartImage.setImageResource(CartImage[position])
                    cartItemQuantity.text = quanlity.toString()
            }
        }
    }
}