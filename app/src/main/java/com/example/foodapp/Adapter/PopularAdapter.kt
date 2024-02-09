package com.example.foodapp.Adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.databinding.PopularItemBinding

class PopularAdapter(private val Items: List<String>,private val price : List<String>,private val nameRestaurant: List<String>, private val image: List<Int>) : RecyclerView.Adapter<PopularAdapter.PoplarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoplarViewHolder {
        return PoplarViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: PoplarViewHolder, position: Int) {
val item = Items[position]
        val image = image[position]
        val nameRestaurant = nameRestaurant[position]
        val price = price[position]
        holder.bind(item,price,nameRestaurant,image)
    }

    override fun getItemCount(): Int {
        return Items.size
    }


    class PoplarViewHolder(private val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private val imagesView = binding.imgFood
        fun bind(item: String,price: String,nameRestaurant: String, image: Int) {
            binding.txtNameFood.text = item
            binding.txtPrice.text = price
            binding.txtNameRestaurant.text = nameRestaurant
            imagesView.setImageResource(image)
        }
    }
}