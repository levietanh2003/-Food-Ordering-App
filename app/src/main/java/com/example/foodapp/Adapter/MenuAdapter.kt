//package com.example.foodapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.DetailsActivity
import com.example.foodapp.Help.formatPrice
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.databinding.MenuItemBinding
import java.util.*

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>(), Filterable {

    private var filteredMenuItems = menuItems.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var countDownTimer: CountDownTimer? = null

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)
                }
            }
        }

        private fun openDetailsActivity(position: Int) {
            val menuItem = menuItems[position]

//            Log.d(
//                "Category",
//                "Category in MenuAdapter: ${menuItem.categoryId}"
//            ) // Kiểm tra giá trị categoryId
//            Log.d(
//                "Discount",
//                "Discount in MenuAdapter: ${menuItem.discountValue}"
//            ) // Kiểm tra giá trị categoryId
//
//            Log.d(
//                "CreatedAt",
//                "CreatedAt in MenuAdapter: ${menuItem.createdAt}"
//            ) // Kiểm tra giá trị categoryId
//            Log.d(
//                "EndAt",
//                "EndAt in MenuAdapter: ${menuItem.discountValue}"
//            ) // Kiểm tra giá trị categoryId
//            Log.d(
//                "FoodId",
//                "FoodId in MenuAdapter: ${menuItem.foodId}"
//            ) // Kiểm tra giá trị categoryId
            val intentDetails = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemId", menuItem.foodId)
                putExtra("MenuItemCreatedAt", menuItem.createdAt)
                putExtra("MenuItemEndAt", menuItem.endAt)
                putExtra("MenuItemDiscount", menuItem.discountValue)
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemIngredient", menuItem.foodIngredient)
                putExtra("MenuItemPrice", menuItem.foodPrice)
                putExtra("MenuItemCategory", menuItem.categoryId)
                putExtra("MenuTypeOfDish", menuItem.typeOfDishId)
            }
            requireContext.startActivity(intentDetails)

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

                // Check if discountValue is not null
                if (menuItem.discountValue != null) {
                    menuDiscount.text = "${menuItem.discountValue}% off"
                    menuDiscount.visibility = View.VISIBLE
                } else {
                    menuDiscount.visibility = View.GONE
                }

                if (menuItem.createdAt != null && menuItem.endAt != null) {
                    val remainingTime =
                        PromotionUtils.getRemainingTime(menuItem.createdAt, menuItem.endAt)
//                    menuPromotionEnd.text =
//                        PromotionUtils.calculatePromotionEnd(menuItem.createdAt, menuItem.endAt)
                    if (remainingTime > 0) {
                        menuPromotionEnd.visibility = View.VISIBLE
                        startCountDown(remainingTime)
                    }
                } else {
                    menuPromotionEnd.visibility = View.GONE
                }
            }
        }

        private fun startCountDown(remainingTime: Long) {
            countDownTimer?.cancel() // Cancel any existing timer

            countDownTimer = object : CountDownTimer(remainingTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val days = millisUntilFinished / (1000 * 60 * 60 * 24)
                    val hours = (millisUntilFinished / (1000 * 60 * 60)) % 24
                    val minutes = (millisUntilFinished / (1000 * 60)) % 60
                    val seconds = (millisUntilFinished / 1000) % 60

                    binding.menuPromotionEnd.text = String.format(
                        Locale.getDefault(),
                        "Ends in %02d:%02d:%02d:%02d",
                        days, hours, minutes, seconds
                    )
                }

                override fun onFinish() {
                    binding.menuPromotionEnd.text = "Promotion ended"
                }
            }.start()
        }
    }


    interface OnClickListener {
        fun onItemClick(position: Int) {

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
                        if (menuItem.foodName?.toLowerCase(Locale.getDefault())!!
                                .contains(searchText) ||
                            menuItem.foodDescription!!.toLowerCase(Locale.getDefault())
                                .contains(searchText) ||
                            menuItem.typeOfDishId!!.toLowerCase(Locale.getDefault())
                                .contains(searchText) ||
                            menuItem.categoryId!!.toLowerCase(Locale.getDefault())
                                .contains(searchText) ||
                            menuItem.discountValue?.toLowerCase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {
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
}
