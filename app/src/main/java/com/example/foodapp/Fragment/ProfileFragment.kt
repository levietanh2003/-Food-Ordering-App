package com.example.foodapp.Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodapp.LoginActivity
import com.example.foodapp.Model.Customer
import com.example.foodapp.R
import com.example.foodapp.Utils.EncryptionUtils

import com.example.foodapp.databinding.ActivityProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import javax.crypto.spec.SecretKeySpec

class ProfileFragment : Fragment() {
    private lateinit var binding: ActivityProfileFragmentBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityProfileFragmentBinding.inflate(inflater, container, false)


        setUpCustomer()
        // chuc nang log out
        binding.linearLogOut.setOnClickListener {
//            Firebase.auth.signOut()
//            val intent = Intent(requireContext(), LoginActivity::class.java)
//            startActivity(intent)
//            activity?.finish()

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Log out")
            builder.setMessage("Are you sure you want to sign out?")
            // Positive button (OK)
            builder.setPositiveButton("Log out") { dialog, which ->

                Firebase.auth.signOut()

                // Start the LoginRegisterActivity
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
            }

            // Show the AlertDialog
            val dialog = builder.create()
            dialog.show()
        }

        // chuyển huong sang các hóa đơn của customer
        binding.linearAllOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderFragment)
        }

        // chuyển sang edit profile
        binding.constraintProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        return binding.root
    }

    private fun showProgressBar() {
        binding.progressbarSettings.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressbarSettings.visibility = View.GONE
    }
    private fun setUpCustomer() {
        val customerId = auth.currentUser?.uid
        if (customerId != null) {
            showProgressBar()
            val customerProfileRef = database.reference.child("customer").child(customerId)
            val keyRef = database.reference.child("keys").child(customerId)

            // Duyệt realtime lấy key
            keyRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val secretKeyString = dataSnapshot.getValue(String::class.java)
                    val secretKey = secretKeyString?.let {
                        val decodedKey = Base64.decode(it, Base64.DEFAULT)
                        SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
                    }

                    if (secretKey != null) {
                        customerProfileRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            @RequiresApi(Build.VERSION_CODES.O)
                            override fun onDataChange(snapshot: DataSnapshot) {
                                hideProgressBar()
                                if (snapshot.exists()) {
                                    val customerProfile = snapshot.getValue(Customer::class.java)

                                    if (customerProfile != null) {
                                        customerProfile.apply {
                                            val decryptedName = EncryptionUtils.decrypt(customerProfile.nameCustomer.toString(), secretKey)
                                            binding.apply {
                                                tvUserName.text = decryptedName
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                hideProgressBar()
                            }
                        })
                    } else {
                        hideProgressBar()
                        // Handle the case when the secret key is null
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    hideProgressBar()
                }
            })
        }
    }


//    private fun setUpCustomer() {
//        var customerId = auth.currentUser?.uid
//        if (customerId != null) {
//            showProgressBar()
//            val customerProfileRef = database.reference.child("customer").child(customerId)
//            val keyRef = database.reference.child("keys").child(customerId)
//
//            // duyet realtime lay key
//            keyRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                    val secretKeyString = dataSnapshot.getValue(String::class.java)
//                    val secretKey = secretKeyString?.let {
//                        val decodedKey = Base64.decode(it, Base64.DEFAULT)
//                        SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
//                    }
//
//
//                    customerProfileRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                        @RequiresApi(Build.VERSION_CODES.O)
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            hideProgressBar()
//                            if (snapshot.exists()) {
//                                val customerProfile = snapshot.getValue(Customer::class.java)
//
//                                if (customerProfile != null) {
//                                    //------------------------//-----------------------//
//                                    customerProfile.apply {
//                                        val test =
//                                            EncryptionUtils.decrypt(customerProfile.nameCustomer.toString(),secretKey)
//                                    }
//                                    //------------------------//-----------------------//
//                                    binding.apply {
//                                        tvUserName.text = customerProfile.nameCustomer
//                                    }
//                                }
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            hideProgressBar()
//                        }
//                    })
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//            })
//        }
//    }
}