package com.example.foodapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.foodapp.Model.Customer
import com.example.foodapp.Utils.EncryptionUtils
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
import javax.crypto.SecretKey

class SignUpActivity : AppCompatActivity() {

    // khai bao thuoc tinh
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var secretKey: SecretKey

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id1)).requestEmail().build()

        // Khởi tạo GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Khởi tạo firebase auth
        auth = Firebase.auth
        // Khởi tạo firebase DataBase
        database = Firebase.database.reference

        // Tạo khóa bí mật cho mã hóa
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            secretKey = EncryptionUtils.generateSecretKey()
        }

        // Thực hiện btn đăng ký
        binding.btnSignup.setOnClickListener {
            // Lấy thông tin trên form
            userName = binding.editTextName.text.toString()
            email = binding.editTextEmailSignup.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            // Kiểm tra dữ liệu đầu vào
            if (email.isBlank() || userName.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.btnGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcer.launch(signIntent)
        }

        // Thao tác ẩn hiện mật khẩu
        binding.icEyeOff.setOnClickListener {
            var isPasswordVisible = false

            val btnicEyeOff = findViewById<ImageView>(R.id.icEyeOff)
            btnicEyeOff.setOnClickListener {
                isPasswordVisible = !isPasswordVisible
                if (isPasswordVisible) {
                    binding.editTextPassword.transformationMethod = null
                    btnicEyeOff.setImageResource(R.drawable.ic_show)
                } else {
                    binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    btnicEyeOff.setImageResource(R.drawable.ic_hide)
                }
            }
        }
    }

    private val launcer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        Toast.makeText(this, "Sign in to your Google account successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
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


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully registered account", Toast.LENGTH_SHORT).show()
                // Lưu thông tin người dùng vào database
                saveUserData()
                // Đăng nhập
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Account registration failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failure", task.exception)
            }
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun saveUserData() {
        userName = binding.editTextName.text.toString()
        email = binding.editTextEmailSignup.text.toString().trim()
        password = binding.editTextPassword.text.toString().trim()

        // save user when encrypt
        val customer = Customer(userName, email, password, null, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            customer.encryptSensitiveData(secretKey)
        }

        val customerId: String = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("customer").child(customerId).setValue(customer)
        //-------------------------------//--------------------------------//
        // Save the secret key in Firebase under the user's ID
        val secretKeyString = Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
        Log.d("Key","KEY : $secretKeyString");
        database.child("keys").child(customerId).setValue(secretKeyString)
        //-------------------------------//--------------------------------//

    }
}
