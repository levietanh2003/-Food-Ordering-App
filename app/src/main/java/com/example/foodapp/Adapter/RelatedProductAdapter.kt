package com.example.foodapp.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.DetailsActivity
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.R
import java.text.NumberFormat
import java.util.*

class RelatedProductAdapter(private val relatedProducts: List<MenuItem>) : RecyclerView.Adapter<RelatedProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById<TextView>(R.id.productName)
        val productPrice = itemView.findViewById<TextView>(R.id.productPrice)
        val productImage = itemView.findViewById<ImageView>(R.id.productImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_related_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = relatedProducts[position]
        holder.productName.text = product.foodName
        holder.productPrice.text = formatPrice(product.foodPrice)
        Glide.with(holder.itemView.context).load(Uri.parse(product.foodImage)).into(holder.productImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", product.foodName)
                putExtra("MenuItemDescription", product.foodDescription)
                putExtra("MenuItemIngredient", product.foodIngredient)
                putExtra("MenuItemPrice", product.foodPrice)
                putExtra("MenuItemImage", product.foodImage)
//                putExtra("MenuTypeOfDish", product.typeOfDish)
//                putExtra("MenuItemCategory", product.category)
//                putExtra("MenuItemDiscount", product.valueDiscount)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return relatedProducts.size
    }

    private fun formatPrice(price: String?): String {
        return try {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            val parsedPrice = price?.toDouble() ?: 0.0
            numberFormat.format(parsedPrice)
        } catch (e: Exception) {
            "0 VNĐ"
        }
    }
}
