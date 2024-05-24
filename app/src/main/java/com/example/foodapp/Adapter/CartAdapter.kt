package com.example.foodapp.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.DetailsActivity
import com.example.foodapp.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.NumberFormat
import java.util.*

@Suppress("DEPRECATION")
class CartAdapter(
    private val requireContext: Context,
    private val CartItems: MutableList<String>,
    private val CartItemPrice: MutableList<String>,
    private var CartDescription: MutableList<String>,
    private var CartImage: MutableList<String>,
    private val CartQuantity: MutableList<Int>,
    private var CartIngredients: MutableList<String>,
    private var typeOfDish: MutableList<String>,
    private var CartDiscount: MutableList<String>,
    private val totalPriceTextView: TextView,


) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    //    private val itemQuantities = IntArray(CartItems.size){1}

    private val auth = FirebaseAuth.getInstance()

    // load gio hang
    init {
        val database = FirebaseDatabase.getInstance()
        val customerId = auth.currentUser?.uid ?: ""
        val cartItemNumber = CartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemsReference =
            database.reference.child("customer").child(customerId).child("CartItems")

    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.CartViewHolder, position: Int) {
        holder.bind(position)

//        updateTotalPrice()

        holder.itemView.setOnClickListener {
            // luong xu ly intent anh
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("FilteredMenuItems", CartItems[position])
            intent.putExtra("FilteredMenuItemImage", CartImage[position])
//            intent.putExtra("FilteredMenuItemImage", CartDescription[position])
//            intent.putExtra("FilteredMenuItemImage", CartItemPrice[position])
//            intent.putExtra("FilteredMenuItemImage", CartIngredients[position])
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return CartItems.size
    }

    // lay so luong phan tu
    fun getUpdateItemsQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(CartQuantity)
        return itemQuantity
    }

    fun getUpdateItemFood(position: Int): Triple<String, String, String> {
        val foodName = CartItems[position]
        val foodPrice = CartItemPrice[position]
        val foodImage = CartImage[position]
        return Triple(foodName, foodPrice, foodImage)
    }

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.apply {
                val quanlitquality = itemQuantities[position]
                cartFoodName.text = CartItems[position]
                //   val cartPrice = CartItemPrice[position].toDouble()
                cartItemPrice.text = formatPrice(CartItemPrice[position])
                typeOfDish.text = CartItems[position]

                if (CartDiscount != null) {
                    cartDiscount.text = "${CartDiscount[position]}%"

                    cartDiscount.visibility = View.VISIBLE
                } else {
                    cartDiscount.visibility = View.GONE
                }
                // load image using Glide
                val uriString = CartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(requireContext).load(uri).into(cartImage)
                cartItemQuantity.text = quanlitquality.toString()
                // Show total price after discount
//                val discountedPrice = cartPrice * (1 - cartDiscount.toDouble())
//                menuPrice.text = formatPrice(discountedPrice.toString())

                btnMinus.setOnClickListener {
                    deceaseQuantity(position)
                }

                btnPlus.setOnClickListener {
                    increaseQuantity(position)
                }
                btnTrash.setOnClickListener {
                    val itemPosition = adapterPosition
                    // kiem tra xem RecyclerView co phan tu nao khong
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem()
                    }
                }
                updateTotalPrice()
            }
        }

        private fun deleteItem() {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                // Xóa phần tử từ danh sách CartItems sau khi xóa khỏi Firebase thành công
                CartItems.removeAt(position)
                CartImage.removeAt(position)
                CartDescription.removeAt(position)
                CartQuantity.removeAt(position)
                CartItemPrice.removeAt(position)
                CartIngredients.removeAt(position)
                typeOfDish.removeAt(position)
                CartDiscount.removeAt(position)
                Toast.makeText(requireContext, "Deleted successfully", Toast.LENGTH_SHORT).show()

                // Cập nhật lại itemQuantities
                itemQuantities =
                    itemQuantities.filterIndexed { index, _ -> index != position }.toIntArray()

                // Thông báo cho Adapter biết về việc xóa phần tử
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, CartItems.size)
                updateTotalPrice()
            }.addOnFailureListener {
                Toast.makeText(requireContext, "Delete failed", Toast.LENGTH_SHORT).show()
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    // loop for snapshot children
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        private fun increaseQuantity(position: Int) {
//            itemQuantities[position]++
//            notifyItemChanged(position)
//            CartQuantity[position] = itemQuantities[position]
//            binding.cartItemQuantity.text = itemQuantities[position].toString()
            itemQuantities[position]++
            CartQuantity[position] = itemQuantities[position]
            notifyDataSetChanged()

            // Get unique key at position and update quantity in Firebase
            getUniqueKeyAtPosition(position) { uniqueKey ->
                uniqueKey?.let { key ->
                    cartItemsReference.child(key).child("foodQuantity")
                        .setValue(itemQuantities[position])
                }
            }
            updateTotalPrice()
        }

        private fun deceaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                CartQuantity[position] = itemQuantities[position]
                notifyItemChanged(position)
                binding.cartItemQuantity.text = itemQuantities[position].toString()

                // Get unique key at position and update quantity in Firebase
                getUniqueKeyAtPosition(position) { uniqueKey ->
                    uniqueKey?.let { key ->
                        cartItemsReference.child(key).child("foodQuantity")
                            .setValue(itemQuantities[position])
                    }
                }
                updateTotalPrice()
            }
        }
    }

     fun updateTotalPrice() {
        var totalPrice = 0.0

        for (i in CartItems.indices) {
            val price = CartItemPrice[i].toDouble()
            val quantity = CartQuantity[i]
            val discount = CartDiscount.getOrNull(i)?.toDoubleOrNull() ?: 0.0
            val discountedPrice = price * (1 - discount / 100)
            totalPrice += discountedPrice * quantity
        }

        val formattedPrice = formatPrice(totalPrice.toString())
        totalPriceTextView.text = formattedPrice
         // Notify the listener about the updated total price

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