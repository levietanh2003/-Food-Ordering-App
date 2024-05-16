//package com.example.foodapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.Adapter.CartAdapter
import com.example.foodapp.DetailsActivity
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.databinding.MenuItemBinding
import java.text.NumberFormat
import java.util.*

class MenuAdapter(
    private val menuItems : List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(), Filterable {
    private var filteredMenuItems = menuItems.toMutableList()

//    private val itemClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]

            Log.d("Category", "Category in MenuAdapter: ${menuItem.categoryId}") // Kiểm tra giá trị categoryId
            Log.d("Discount", "Discount in MenuAdapter: ${menuItem.discountValue}") // Kiểm tra giá trị categoryId


            val intentDetails = Intent(requireContext,DetailsActivity::class.java).apply {
                putExtra("MenuItemDiscount",menuItem.discountValue)
                putExtra("MenuItemName",menuItem.foodName)
                putExtra("MenuItemImage",menuItem.foodImage)
                putExtra("MenuItemDescription",menuItem.foodDescription)
                putExtra("MenuItemIngredient",menuItem.foodIngredient)
                putExtra("MenuItemPrice",menuItem.foodPrice)
                putExtra("MenuItemCategory",menuItem.categoryId)
                putExtra("MenuTypeOfDish",menuItem.typeOfDishId)
            }
            requireContext.startActivity(intentDetails)


//            val intentCart = Intent(requireContext,CartAdapter::class.java).apply {
//                putExtra("MenuItemDiscount",menuItem.discountValue)
//                putExtra("MenuItemName",menuItem.foodName)
//                putExtra("MenuItemImage",menuItem.foodImage)
//                putExtra("MenuItemDescription",menuItem.foodDescription)
//                putExtra("MenuItemIngredient",menuItem.foodIngredient)
//                putExtra("MenuItemPrice",menuItem.foodPrice)
//                putExtra("MenuItemCategory",menuItem.categoryId)
//                putExtra("MenuTypeOfDish",menuItem.typeOfDishId)
//            }
//            requireContext.startActivity(intentCart)
        }



        // set data in to recyclerview items name, price, image, type of dish
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuNameFood.text = menuItem.foodName
                menuPrice.text = formatPrice(menuItem.foodPrice)
                menuTypeOfDish.text = menuItem.typeOfDishId
                menuCategory.text = menuItem.categoryId
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }

    interface OnClickListener {
        fun onItemClick(position: Int){

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchText = constraint.toString().lowercase(Locale.getDefault())
                val results = FilterResults()

                if (searchText.isEmpty()) {
                    filteredMenuItems = menuItems.toMutableList()
                } else {
                    val filteredList = mutableListOf<MenuItem>()
                    for (menuItem in menuItems) {
                        if (menuItem.foodName?.toLowerCase(Locale.getDefault())!!.contains(searchText) ||
                            menuItem.foodDescription!!.toLowerCase(Locale.getDefault()).contains(searchText) ||
                            menuItem.typeOfDishId!!.toLowerCase(Locale.getDefault()).contains(searchText) ||
                            menuItem.categoryId!!.toLowerCase(Locale.getDefault()).contains(searchText) ||
                            menuItem.discountValue!!.toLowerCase(Locale.getDefault()).contains(searchText) ) {
                            filteredList.add(menuItem)
                        }
                    }
                    filteredMenuItems = filteredList
                }

                results.values = filteredMenuItems
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredMenuItems = results?.values as MutableList<MenuItem>
                notifyDataSetChanged()
            }
        }
    }
    private fun formatPrice(price: String?): String {
        return try {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            val parsedPrice = price?.toDouble() ?: 0.0
            numberFormat.format(parsedPrice)
        } catch (e: Exception) {
            "0 VNĐ" // Trả về mặc định nếu không thể định dạng giá
        }
    }
}
