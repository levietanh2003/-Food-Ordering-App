package com.example.foodapp.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodapp.Model.Customer
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityEditProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditProfileFragment : Fragment() {

    private lateinit var binding: ActivityEditProfileFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityEditProfileFragmentBinding.inflate(inflater)


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        setUpCustomer()

        // chức năng quên mật khẩu
        binding.tvUpdatePassword.setOnClickListener {
            val email = binding.profileEmail.text.toString()
            resetPassword(email)
        }
        // chức năng lưu thông tin
        binding.btnSaveInformation.setOnClickListener {
            val name = binding.profileName.text.toString()
            val email = binding.profileEmail.text.toString()
            val address = binding.profileAddress.text.toString()
            val phone = binding.profilePhone.text.toString()

            if (name.isBlank() || email.isBlank() || address.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(
                    context,
                    "Please fill in all information",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // update data
                updateCustomerProfile(name, email, address, phone)
            }
        }

        // chuyển về ProfileFragment
        binding.imageCloseUserAccount.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }

        return binding.root
    }

    private fun setUpCustomer() {
        val customerId = auth.currentUser?.uid
        if (customerId != null) {
            val customerProfileRef = database.reference.child("customer").child(customerId)

            customerProfileRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val customerProfile = snapshot.getValue(Customer::class.java)

                        if (customerProfile != null) {
                            binding.apply {
                                profileName.setText(customerProfile.nameCustomer)
                                profileAddress.setText(customerProfile.addressCustomer)
                                profileEmail.setText(customerProfile.emailCustomer)
                                profilePhone.setText(customerProfile.phoneNumberCustomer)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun updateCustomerProfile(name: String, email: String, address: String, phone: String) {
        val customerId = auth.currentUser?.uid
        if (customerId != null) {
            val customerProfileRef = database.getReference("customer").child(customerId)

            val customerData = hashMapOf(
                "nameCustomer" to name,
                "addressCustomer" to address,
                "emailCustomer" to email,
                "phoneNumberCustomer" to phone
            )
            // Chuyển đổi HashMap của Kotlin thành java.util.HashMap để khớp với kiểu dữ liệu của phương thức
            val javaCustomerData: java.util.HashMap<String, Any> = HashMap(customerData)

            customerProfileRef.updateChildren(javaCustomerData).addOnFailureListener {
                Toast.makeText(context, "Profile Update Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "Check your email to recover your password $email",
                    Toast.LENGTH_SHORT
                )
                    .show()

            } else {
                Toast.makeText(
                    context,
                    "Error! An error occurred. Please try again later",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}