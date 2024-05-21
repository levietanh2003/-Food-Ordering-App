package com.example.foodapp.Fragment

import MenuAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodapp.Model.MenuItem
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivitySearchFragmentBinding
import com.google.firebase.database.*

class SearchFragment : Fragment() {
    private lateinit var binding: ActivitySearchFragmentBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val orignelMenuItems = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySearchFragmentBinding.inflate(inflater, container, false)

        retrieveMenuItem()
        setUpSearchView()
        return binding.root
    }

    private fun retrieveMenuItem() {
        // get database reference
        database = FirebaseDatabase.getInstance()
        // reference to the Menu node
        val foodRef: DatabaseReference = database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val inStock = snapshot.child("inStock").getValue(Boolean::class.java)
                    if (inStock == true) {
                        val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                        menuItem?.let {
                            orignelMenuItems.add(it)
                        }
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showAllMenu() {
        val filteredMenuItem = ArrayList(orignelMenuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: java.util.ArrayList<MenuItem>) {
        adapter = MenuAdapter(filteredMenuItem, requireContext())
        binding.menuRecyclerViews.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerViews.adapter = adapter
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return false
            }
        })
    }
    private fun filterMenuItems(query: String){
        val filteredMenuItem = orignelMenuItems.filter {
            it.foodName?.contains(query,ignoreCase = true) == true
        }
        setAdapter(filteredMenuItem as java.util.ArrayList<MenuItem>)
    }
}
