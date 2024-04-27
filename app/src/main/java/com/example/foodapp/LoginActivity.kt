package com.example.foodapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodapp.Model.Customer
import com.example.foodapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var userName : String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id1)).requestEmail().build()

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // khoi tao firebase
        auth = Firebase.auth
        // khoi tao database
        database = Firebase.database.reference

        // thao tac su kien btn_login
        binding.btnLogin.setOnClickListener {
            // xu ly lay du lieu dau vao va kiem tra
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email và mật khẩu ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                createUser()
            }
        }
        // test reset password
        binding.btnFacebook.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            if(email.isBlank()){
                Toast.makeText(this, "Vui lòng nhập email ", Toast.LENGTH_SHORT).show()
            }else{
                resetPassword(email)
            }
        }

        // thao tac khi chua co tai khoan
        binding.doNotHaveAccount.setOnClickListener {
            // xu ly
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // đăng nhập bằng google
        binding.btnGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcer.launch(signIntent)
        }
    }

    // cấu hình login google
    private val launcer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            //handleSignInResult(task)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if(authTask.isSuccessful) {
                        // dang nhap thanh cong bang tai khoan google
                        Toast.makeText(this, "Đăng nhập tài khoản Google thành công", Toast.LENGTH_SHORT).show()
                        updateUI(authTask.result?.user)
                        //startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createUser() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        val customersRef = database.child("customer")
        customersRef.orderByChild("emailCustomer").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (customerSnapshot in dataSnapshot.children) {
                        val customer = customerSnapshot.getValue(Customer::class.java)
                        if (customer?.passwordCustomer == password) {
                            // Xác thực thành công
                            val currentUser = auth.currentUser
                            updateUI(currentUser)
                            return
                        } else {
                            // Mật khẩu không đúng
                            Toast.makeText(this@LoginActivity, "Mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                } else {
                    // Email không tồn tại trong cơ sở dữ liệu
                    Toast.makeText(this@LoginActivity, "Email không tồn tại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Xử lý khi có lỗi xảy ra trong quá trình truy vấn cơ sở dữ liệu
                Toast.makeText(this@LoginActivity, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // kiem tra phien luot dang nhap
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateUI(customer: FirebaseUser?) {
        var intent = Intent(this,MainActivity::class.java)
        Toast.makeText(this, "Đăng nhập thành công ", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }

    // ham reset password
    fun resetPassword(email: String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(this@LoginActivity, "Kiểm tra email để khôi phục mật khẩu " + email , Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(this@LoginActivity, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
