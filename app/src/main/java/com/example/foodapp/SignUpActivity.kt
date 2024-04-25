package com.example.foodapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodapp.Model.Customer
import com.example.foodapp.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class SignUpActivity : AppCompatActivity() {

    // khai bao thuoc tinh
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id1)).requestEmail().build()

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        // Khiến GoogleSignInClient không bằng null
//        if (googleSignInClient == null) {
//            Toast.makeText(this, "Không thể khởi tạo GoogleSignInClient", Toast.LENGTH_SHORT).show()
//            finish() // Kết thúc hoạt động nếu không thể khởi tạo
//            return
//        }
        // khoi tao firebase auth
        auth = Firebase.auth
        // khoi tao firebase DataBase
        database = Firebase.database.reference

        // thuc hien btn dang ki
        binding.btnSignup.setOnClickListener {
            // lay thong tin tren form
            userName = binding.editTextName.text.toString()
            email = binding.editTextEmailSignup.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            // kiem tra du lieu dau vao
            if (email.isBlank() || userName.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.btnGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcer.launch(signIntent)
        }
        // thao tác ẩn hiện mât khẩu
        // Sự kiện khi người dùng nhấn vào biểu tượng mắt
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
                    binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    btnicEyeOff.setImageResource(R.drawable.ic_hide)
                }
            }
        }
    }

    private val launcer =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                //handleSignInResult(task)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // dang nhap thanh cong bang tai khoan google
                            Toast.makeText(
                                this,
                                "Đăng nhập tài khoản Google thành công",
                                Toast.LENGTH_SHORT
                            ).show()
//                        updateUI(authTask.result?.user)
//                            val intent = Intent(this, LoginActivity::class.java)
//                            startActivity(intent)

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // kiem tra nguoi dung dang nhap da dang nhap chua
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    // kiem tra neu da dang nhap thi cho login vao main
    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show()
                // luu thong tin nguoi dung vao database
                saveUserData()
                // dong thoi cho nguoi dung dang nhap
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Đăng kí tài khoản thất bại", Toast.LENGTH_SHORT).show()
                // log hien loi
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    private fun saveUserData() {
        userName = binding.editTextName.text.toString()
        email = binding.editTextEmailSignup.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()

        // khoi tao truyen gia tri
        val customer = Customer(userName,email,password)
        // khoi tao id
        val customerId : String  = FirebaseAuth.getInstance().currentUser!!.uid
        // luu du lieu xuong database
        database.child("customer").child(customerId).setValue(customer)
    }
}