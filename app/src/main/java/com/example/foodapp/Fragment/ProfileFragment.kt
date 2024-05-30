package com.example.foodapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.foodapp.LoginActivity
import com.example.foodapp.Model.Customer
import com.example.foodapp.R

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


        setUpCustomer()
        // chuc nang log out
        binding.linearLogOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
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
        var customerId = auth.currentUser?.uid
        if (customerId != null) {
            showProgressBar()
            val customerProfileRef = database.reference.child("customer").child(customerId)

            customerProfileRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    hideProgressBar()
                    if (snapshot.exists()) {
                        val customerProfile = snapshot.getValue(Customer::class.java)

                        if (customerProfile != null) {
                            binding.apply {
                                tvUserName.text = customerProfile.nameCustomer
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    hideProgressBar()
                }
            })
        }
    }
}