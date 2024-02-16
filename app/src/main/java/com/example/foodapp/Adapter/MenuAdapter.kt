import android.content.Context
import android.content.Intent
import android.widget.Filter
import android.widget.Filterable

//package com.example.foodapp.Adapter

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.DetailsActivity
import com.example.foodapp.databinding.MenuItemBinding
import java.util.*

class MenuAdapter(
    private var originalMenuItems: MutableList<String>,
    private var originalMenuItemPrice: MutableList<String>,
    private var originalMenuItemImage: MutableList<Int>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(), Filterable {

    private var filteredMenuItems: MutableList<String> = originalMenuItems.toMutableList()
    private var filteredMenuItemPrice: MutableList<String> = originalMenuItemPrice.toMutableList()
    private var filteredMenuItemImage: MutableList<Int> = originalMenuItemImage.toMutableList()


    private val itemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = filteredMenuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // kiem tra phan tu tra ve co null hay khong
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemClickListener?.onItemClick(position)
                }
                // luong xu ly intent anh
                val intent = Intent(requireContext, DetailsActivity::class.java)
                intent.putExtra("MenuItemName", filteredMenuItems[position])
                intent.putExtra("MenuItemImage", filteredMenuItemImage[position])
                requireContext.startActivity(intent)
            }
        }
        fun bind(position: Int) {
            binding.menuNameFood.text = filteredMenuItems[position]
            binding.menuPrice.text = filteredMenuItemPrice[position]
            binding.menuImage.setImageResource(filteredMenuItemImage[position])
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchText = constraint.toString().toLowerCase(Locale.getDefault())
                if (searchText.isEmpty()) {
                    filteredMenuItems = originalMenuItems.toMutableList()
                    filteredMenuItemPrice = originalMenuItemPrice.toMutableList()
                    filteredMenuItemImage = originalMenuItemImage.toMutableList()
                } else {
                    val filteredListItems = mutableListOf<String>()
                    val filteredListPrices = mutableListOf<String>()
                    val filteredListImages = mutableListOf<Int>()

                    for (i in originalMenuItems.indices) {
                        if (originalMenuItems[i].toLowerCase(Locale.getDefault()).contains(searchText)) {
                            filteredListItems.add(originalMenuItems[i])
                            filteredListPrices.add(originalMenuItemPrice[i])
                            filteredListImages.add(originalMenuItemImage[i])
                        }
                    }
                    filteredMenuItems = filteredListItems
                    filteredMenuItemPrice = filteredListPrices
                    filteredMenuItemImage = filteredListImages
                }

                val filterResults = FilterResults()
                filterResults.values = filteredMenuItems
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMenuItems = results?.values as MutableList<String>
                notifyDataSetChanged()
            }
        }
    }
    interface OnClickListener {
        fun onItemClick(position: Int){

        }
    }
}
