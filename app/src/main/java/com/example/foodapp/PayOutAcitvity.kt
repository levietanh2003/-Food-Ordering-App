package com.example.foodapp

//import vn.momo.momo_partner.AppMoMoLib

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.foodapp.Help.formatPrice
import com.example.foodapp.Model.OrderDetails
import com.example.foodapp.databinding.ActivityPayOutAcitvityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import org.json.JSONException
import org.json.JSONObject
import vn.momo.momo_partner.AppMoMoLib
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

    // payment momo
//    private val amount = "1000000"
    private val fee = "0"
    private val environment = 0 // developer default
    private val merchantName = "Demo SDK"
    private val merchantCode = "SCB01"
    private val merchantNameLabel = "Nhà cung cấp"
    private val description = "Thanh toán dịch vụ ABC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutAcitvityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize MoMo SDK
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)

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
        binding.btnPlaceMyOrderMomo.setOnClickListener {
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
            } else {
                placeOrderAndRequestPayment()
            }
        }

        // su kien tro ve
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    // Hàm này tạo và hiển thị mã QR trên ImageView
    // Hàm này tạo và hiển thị mã QR trên ImageView từ dữ liệu đơn hàng
    fun generateAndDisplayQRCode(orderDetails: OrderDetails, imageView: ImageView, name: String) {
        // Chuẩn bị dữ liệu đơn hàng thành một chuỗi
        val orderData =
            "${orderDetails.customerId},${name},${orderDetails.address},${orderDetails.phoneNumber},${orderDetails.totalPrice},${orderDetails.note}"

        try {
            // Tạo đối tượng BitMatrix từ dữ liệu đơn hàng và các thông số cần thiết
            val bitMatrix = MultiFormatWriter().encode(
                orderData, BarcodeFormat.QR_CODE, 500, 500, mapOf(EncodeHintType.MARGIN to 1)
            )

            // Chuyển đổi BitMatrix thành Bitmap
            val bitmap = bitMatrixToBitmap(bitMatrix)

            // Hiển thị Bitmap trên ImageView
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    // Hàm này chuyển đổi BitMatrix thành Bitmap
    private fun bitMatrixToBitmap(bitMatrix: BitMatrix): Bitmap {
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        // Duyệt qua từng pixel của BitMatrix và đặt giá trị màu tương ứng cho Bitmap
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
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

    // payment momo
//    private fun placeOrderAndRequestPayment() {
//        customerId = auth.currentUser?.uid ?: ""
//        val spinnerPaymentMethod = findViewById<Spinner>(R.id.spinnerPaymentMethod)
//        val paymentStatus = savePaymentStatus(spinnerPaymentMethod)
//
//        val totalPayment = totalPrice
//        val time = System.currentTimeMillis()
//        val itemPushKey = databaseReference.child("OrderDetails").push().key
//        val orderDetails = OrderDetails(
//            customerId,
//            name,
//            foodItemName,
//            foodItemPrice,
//            foodItemImages,
//            foodItemQuantiles,
//            totalPayment,
//            note,
//            address,
//            phone,
//            time,
//            paymentStatus,
//            itemPushKey
//        )
//        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
//        orderReference.setValue(orderDetails).addOnSuccessListener {
//            orderId = orderReference.key.toString()
//
////            val bottomSheetDialog = CongratsBottomSheet()
////            bottomSheetDialog.show(supportFragmentManager, "Test")
//            val qrCodeImageView = findViewById<ImageView>(R.id.qrCodeImageView)
////            generateAndDisplayQRCode(orderDetails, qrCodeImageView,name)
//            // thanh toan momo
//            requestPayment(orderId)
//            // delete item in cart
//            removeItemFromCart()
//            addOrderToHistory(orderDetails)
//
////            Log.d("OrderDetails", "OrderDetails : ${orderId}")
//        }.addOnFailureListener {
//            Toast.makeText(this, "Failed to order", Toast.LENGTH_SHORT).show()
//        }
//    }


    // payment ship code
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
    private fun requestPayment(orderId: String) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)

        val eventValue: MutableMap<String, Any> = HashMap()
        //client Required
        eventValue["merchantname"] =
            merchantName //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue["merchantcode"] =
            merchantCode //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue["amount"] = totalPrice //Kiểu integer
        eventValue["orderId"] =
            orderId //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue["orderLabel"] = orderId //gán nhãn

        //client Optional - bill info
        eventValue["merchantnamelabel"] = "Dịch vụ" //gán nhãn
        eventValue["fee"] = "0" //Kiểu integer
        eventValue["description"] = description //mô tả đơn hàng - short description

        //client extra data
        eventValue["requestId"] = merchantCode + "merchant_billId_" + System.currentTimeMillis()
        eventValue["partnerCode"] = merchantCode
        //Example extra data
        val objExtraData = JSONObject()
        try {
            objExtraData.put("site_code", "008")
            objExtraData.put("site_name", "CGV Cresent Mall")
            objExtraData.put("screen_code", 0)
            objExtraData.put("screen_name", "Special")
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3")
            objExtraData.put("movie_format", "2D")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        eventValue["extraData"] = objExtraData.toString()

        eventValue["extra"] = ""
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue)
    }

    //Get token callback from MoMo app an submit to server side
    // Di chuyển phần đẩy đơn hàng vào cơ sở dữ liệu từ placeOrderAndRequestPayment() sang onActivityResult()
    private fun placeOrderAndRequestPayment() {
        customerId = auth.currentUser?.uid ?: ""
        val spinnerPaymentMethod = findViewById<Spinner>(R.id.spinnerPaymentMethod)
        val paymentStatus = savePaymentStatus(spinnerPaymentMethod)

        val totalPayment = totalPrice
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        orderId = itemPushKey.toString() // Lưu orderId tạm thời ở đây để sử dụng sau khi thanh toán

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

        // Gọi hàm requestPayment() để bắt đầu quá trình thanh toán
        requestPayment(orderId)
    }

    // Xử lý kết quả từ MoMo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                when (data.getIntExtra("status", -1)) {
                    0 -> {
                        // Thanh toán thành công, tiến hành đẩy đơn hàng vào cơ sở dữ liệu
                        val itemPushKey = orderId // Lấy orderId đã lưu từ trước
                        val orderDetails = OrderDetails(
                            customerId,
                            name,
                            foodItemName,
                            foodItemPrice,
                            foodItemImages,
                            foodItemQuantiles,
                            totalPrice,
                            note,
                            address,
                            phone,
                            System.currentTimeMillis(),
                            savePaymentStatus(findViewById<Spinner>(R.id.spinnerPaymentMethod)),
                            itemPushKey
                        )
                        val orderReference =
                            databaseReference.child("OrderDetails").child(itemPushKey!!)
                        orderReference.setValue(orderDetails).addOnSuccessListener {
                            // Xóa các món hàng trong giỏ hàng
                            removeItemFromCart()
                            // Thêm đơn hàng vào lịch sử mua hàng của khách hàng
                            addOrderToHistory(orderDetails)
                            // Hiển thị thông báo thành công hoặc thực hiện các công việc khác
                            val bottomSheetDialog = CongratsBottomSheet()
                            bottomSheetDialog.show(supportFragmentManager, "Test")
                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to order", Toast.LENGTH_SHORT).show()
                        }
                    }
                    1 -> {
                        // Thanh toán thất bại
                        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Các trường hợp khác
                        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Khi data trả về từ MoMo là null
                Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Khi requestCode hoặc resultCode không phù hợp
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show()
        }
    }
}