package com.example.foodapp.Fragment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodapp.LoginActivity
import com.example.foodapp.Model.Customer
import com.example.foodapp.R
import com.example.foodapp.SignUpActivity
import com.example.foodapp.databinding.ActivityProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

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

        // load profile customer
        setUpCustomer()
        //  su ly su kien button saveInformation
        binding.btnSaveInformation.setOnClickListener {
            val name = binding.profileName.text.toString()
            val email = binding.profileEmail.text.toString()
            val address = binding.profileAddress.text.toString()
            val phone = binding.profilePhone.text.toString()

            if (name.isBlank() || email.isBlank() || address.isBlank() || address.isBlank() || phone.isBlank()) {
                Toast.makeText(
                    requireContext(),
                    "Please fill in all information",
                    Toast.LENGTH_SHORT
                )
            } else {
                // update data
                updateCustomerProfile(name, email, address, phone)
            }
        }

        binding.btnViewOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_allOrderFragment)
        }

        binding.btnViewOrders2.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_orderFragment)
        }

        binding.btnLogOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
//        action_profileFragment_to_orderFragment
        return binding.root
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
                Toast.makeText(requireContext(), "Profile Update Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun setUpCustomer() {
        var customerId = auth.currentUser?.uid
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
}