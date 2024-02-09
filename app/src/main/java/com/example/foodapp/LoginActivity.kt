package com.example.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // thao tac su kien btn_login
        binding.btnLogin.setOnClickListener {
            // xu ly
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // thao tac khi chua co tai khoan
        binding.doNotHaveAccount.setOnClickListener {
            // xu ly
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}