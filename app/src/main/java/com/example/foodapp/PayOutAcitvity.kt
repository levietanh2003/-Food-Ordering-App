package com.example.foodapp

//import vn.momo.momo_partner.AppMoMoLib

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.Help.formatPrice
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.databinding.ActivityPayOutAcitvityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class PayOutAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutAcitvityBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var totalAmount: String
    private lateinit var foodItemName: ArrayList<String>
    private lateinit var foodItemPrice: ArrayList<String>
    private lateinit var foodItemImages: ArrayList<String>
    private lateinit var foodItemQuantiles: ArrayList<Int>
    private lateinit var note: String
    private lateinit var customerId: String
    private lateinit var orderId: String
    private lateinit var totalPrice: String

//    private val amount = "10000"
//    private val fee = "0"
//    var environment = 0 //developer default
//
//    private val merchantName = "Thanh toán đơn hàng"
//    private val merchantCode = "SCB01"
//    private val merchantNameLabel = "LEVIETANH"
//    private val description = "Mua hàng online"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Moi truong phat trien momo
//        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION

//        val policy = ThreadPolicy.Builder().permitAll().build()
//        StrictMode.setThreadPolicy(policy)
        // ZaloPay SDK Init
//        ZaloPaySDK.init(2553, Environment.SANDBOX)


        auth = FirebaseAuth.getInstance()

        databaseReference = FirebaseDatabase.getInstance().reference
        // set customer data
        setUpdate()

        // get customer details form Firebase
        val intent = intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemQuantiles = intent.getIntegerArrayListExtra("FoodItemQuantiles") as ArrayList<Int>
        foodItemImages = intent.getStringArrayListExtra("FoodItemImages") as ArrayList<String>
        totalPrice = intent.getStringExtra("FoodItemTotalPrice") as String

        // Log các giá trị để kiểm tra
//        Log.d("PayOutActivity", "foodItemName: $foodItemName")
//        Log.d("PayOutActivity", "foodItemPrice: $foodItemPrice")
//        Log.d("PayOutActivity", "foodItemQuantiles: $foodItemQuantiles")
//        Log.d("PayOutActivity", "foodItemImages: $foodItemQuantiles")
//        Log.d("PayOutActivity", "FoodItemTotalPrice: $totalPrice")


//        totalAmount = formatPrice(calculateTotalAmount().toString())
        //binding.payoutTotalAmount.isEnabled = false
        binding.payoutTotalAmount.text = formatPrice(totalPrice)

        // setUp Spinner
        setupPaymentMethodSpinner()
        // button thanh toan
        binding.btnPlaceMyOrder.setOnClickListener {
            name = binding.payOutName.text.toString().trim()
            address = binding.payOutAddress.text.toString().trim()
            phone = binding.payOutPhone.text.toString().trim()
            note = binding.payOutNote.text.toString().trim()

            if (note.isBlank()) {
                note = " " // hoặc có thể gán bằng chuỗi rỗng ""
            }

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please Enter All The Details", Toast.LENGTH_SHORT).show()
            } else {
                placeOrder()
            }
        }

        // thanh toan zalopay
        binding.btnPlaceMyOrderZaloPay.setOnClickListener {
            // input information
            name = binding.payOutName.text.toString().trim()
            address = binding.payOutAddress.text.toString().trim()
            phone = binding.payOutPhone.text.toString().trim()
            note = binding.payOutNote.text.toString().trim()
            totalAmount = totalPrice

            if (note.isBlank()) {
                note = " " // hoặc có thể gán bằng chuỗi rỗng ""
            }

            if (name.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(this, "Please Enter All The Details", Toast.LENGTH_SHORT).show()
            }
        }

        // su kien tro ve
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // lựa chọn phương thức thanh toán
    private fun setupPaymentMethodSpinner() {
        // Khai báo Spinner và danh sách phương thức thanh toán
        val spinnerPaymentMethod: Spinner = findViewById(R.id.spinnerPaymentMethod)
        val paymentMethods = arrayOf("COD", "Bank")

        // Tạo Adapter và thiết lập cho Spinner
        val paymentMethodAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        paymentMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPaymentMethod.adapter = paymentMethodAdapter
    }

    private fun savePaymentStatus(spinner: Spinner): String {
        return spinner.selectedItem.toString()
    }


    private fun placeOrder() {
        customerId = auth.currentUser?.uid ?: ""
//        val orderId  = generateOrderId()
        val spinnerPaymentMethod = findViewById<Spinner>(R.id.spinnerPaymentMethod)
        val paymentStatus = savePaymentStatus(spinnerPaymentMethod)

        val totalPayment = totalPrice
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            customerId,
            name,
            foodItemName,
            foodItemPrice,
            foodItemImages,
            foodItemQuantiles,
            totalPayment,
            note,
            address,
            phone,
            time,
            paymentStatus,
            itemPushKey
        )
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            orderId = orderReference.key.toString()
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager, "Test")
            // delete item in cart
            removeItemFromCart()
            addOrderToHistory(orderDetails)

//            Log.d("OrderDetails", "OrderDetails : ${orderId}")
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to order", Toast.LENGTH_SHORT).show()
        }
    }

    // sau khi thanh toan add hoa don vao lich su hoa don cua customer
    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("customer").child(customerId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }
    }

    private fun removeItemFromCart() {
        // vao gio hang cua customer do xoa di cart khi dat hang
        val cartItemsRef = databaseReference.child("customer").child(customerId).child("CartItems")
        cartItemsRef.removeValue()
    }

    private fun setUpdate() {
        val customer = auth.currentUser
        if (customer != null) {
            val customerId = customer.uid
            val customerRef = databaseReference.child("customer").child(customerId)

            customerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val names =
                            snapshot.child("nameCustomer").getValue(String::class.java) ?: ""
                        val address =
                            snapshot.child("addressCustomer").getValue(String::class.java) ?: ""
                        val phones =
                            snapshot.child("phoneNumberCustomer").getValue(String::class.java) ?: ""
                        binding.apply {
                            payOutName.setText(names)
                            payOutAddress.setText(address)
                            payOutPhone.setText(phones)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
    //Get token through MoMo app
//    private fun requestPayment(orderId : String) {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
//
//
//
//        val eventValue: MutableMap<String, Any> = HashMap()
//        //client Required
//        eventValue["merchantname"] = merchantName //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue["merchantcode"] = merchantCode //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue["amount"] = amount //Kiểu integer
//        eventValue["orderId"] = orderId //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
//        eventValue["orderLabel"] = orderId //gán nhãn
//
//        //client Optional - bill info
//        eventValue["merchantnamelabel"] = "Dịch vụ" //gán nhãn
//        eventValue["fee"] = total_fee //Kiểu integer
//        eventValue["description"] = description //mô tả đơn hàng - short description
//
//        //client extra data
//        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
//        eventValue["partnerCode"] = merchantCode
//        //Example extra data
//        val objExtraData = JSONObject()
//        try {
//            objExtraData.put("site_code", "008")
//            objExtraData.put("site_name", "CGV Cresent Mall")
//            objExtraData.put("screen_code", 0)
//            objExtraData.put("screen_name", "Special")
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
//            objExtraData.put("movie_format", "2D")
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        eventValue["extraData"] = objExtraData.toString()
//
//        eventValue["extra"] = ""
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
//    }

    //Get token callback from MoMo app an submit to server side
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if (data != null) {
//                when (data.getIntExtra("status", -1)) {
//                    0 -> {
//                        //TOKEN IS AVAILABLE
//                        tvMessage.text = "message: Get token ${data.getStringExtra("message")}"
//                        val token = data.getStringExtra("data") //Token response
//                        val phoneNumber = data.getStringExtra("phonenumber")
//                        var env = data.getStringExtra("env")
//                        if (env == null) {
//                            env = "app"
//                        }
//                        if (!token.isNullOrEmpty()) {
//                            // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                            // IF Momo topup success, continue to process your order
//                        } else {
//                            tvMessage.text = "message: ${getString(R.string.not_receive_info)}"
//                        }
//                    }
//                    1 -> {
//                        //TOKEN FAIL
//                        val message = data.getStringExtra("message") ?: "Thất bại"
//                        tvMessage.text = "message: $message"
//                    }
//                    2 -> tvMessage.text = "message: ${getString(R.string.not_receive_info)}"
//                    else -> tvMessage.text = "message: ${getString(R.string.not_receive_info)}"
//                }
//            } else {
//                tvMessage.text = "message: ${getString(R.string.not_receive_info)}"
//            }
//        } else {
//            tvMessage.text = "message: ${getString(R.string.not_receive_info_err)}"
//        }
//    }
}