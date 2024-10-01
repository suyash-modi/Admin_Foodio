package com.droid.adminfoodio

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.adminfoodio.Adapters.menuItemRVadapter
import com.droid.adminfoodio.databinding.ActivityAllItemBinding
import com.droid.adminfoodio.models.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()

    private  val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.imageButton.setOnClickListener {
            finish()
        }
        databaseReference = FirebaseDatabase.getInstance().reference
        retreiveMenuItem()

    }



    private fun retreiveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child( "menu")
                 // fetch data from data base
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange (snapshot: DataSnapshot) {
                // Clear existing data before populating
                menuItems.clear()
                // loop for through each food item
                for (foodSnapshot: DataSnapshot in snapshot.children) {
                    val menuItem: AllMenu? = foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError", "Error : ${error.message} ")
            }
        })
        }

    private fun setAdapter() {
        val adapter=menuItemRVadapter(this,menuItems,databaseReference){ position ->
            deleteMenuItem(position)
        }
        binding.allItemRv.layoutManager= LinearLayoutManager(this)
        binding.allItemRv.adapter=adapter
    }

    private fun deleteMenuItem(position: Int) {
        val menuItemToDelete=menuItems[position]
        val menuItemKey=menuItemToDelete.key
        val menuRef=database.reference.child("menu").child(menuItemKey!!)
        menuRef.removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){
                menuItems.removeAt(position)
                binding.allItemRv.adapter?.notifyItemRemoved(position)
            }
            else{
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()

            }
        }


    }
}