package com.example.foodapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
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

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

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
            email = binding.editTextEmail.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                createUser()
            }
        }
        // test reset password
        binding.btnFacebook.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            if (email.isBlank()) {
                Toast.makeText(this, "Please enter email ", Toast.LENGTH_SHORT).show()
            } else {
                resetPassword(email)
            }
        }

        binding.icEyeOff.setOnClickListener {
            var isPasswordVisible = false

            // Ánh xạ ImageView và gắn sự kiện click
            val btnicEyeOff = findViewById<ImageView>(R.id.icEyeOff)
            btnicEyeOff.setOnClickListener {
                // Khi người dùng nhấn vào btnicEyeOff, thực hiện ẩn hoặc hiện mật khẩu
                isPasswordVisible = !isPasswordVisible
                if (isPasswordVisible) {
                    // Nếu mật khẩu đang ẩn, hiện mật khẩu
                    binding.editTextPassword.transformationMethod = null
                    btnicEyeOff.setImageResource(R.drawable.ic_show)
                } else {
                    // Ngược lại, ẩn mật khẩu
                    binding.editTextPassword.transformationMethod =
                        PasswordTransformationMethod.getInstance()
                    btnicEyeOff.setImageResource(R.drawable.ic_hide)
                }
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
    private val launcer =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // dang nhap thanh cong bang tai khoan google
                            Toast.makeText(
                                this,
                                "Sign in to your Google account successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(authTask.result?.user)
                            if (googleSignInAccount != null) {
                                val customer = Customer(
                                    googleSignInAccount.displayName,
                                    googleSignInAccount.email,
                                    "NULL",
                                    "",
                                    ""
                                )
                                val customerId: String =
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                // luu du lieu xuong database
                                database.child("customer").child(customerId).setValue(customer)
                            }
                            finish()
                        } else {
                            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun createUser() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Đăng nhập thành công
                Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                if (user != null) {
                    // Thực hiện các hành động bổ sung sau khi đăng nhập thành công, ví dụ: chuyển hướng đến màn hình chính
                    updateUI(user)
                }
            } else {
                // Đăng nhập thất bại, xử lý thông báo cho người dùng
                Toast.makeText(
                    this,
                    "Đăng nhập thất bại: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
        var intent = Intent(this, MainActivity::class.java)
        Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }

    // ham reset password
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@LoginActivity,
                    "Check your email to recover your password $email",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                Toast.makeText(this@LoginActivity, "Error! An error occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
