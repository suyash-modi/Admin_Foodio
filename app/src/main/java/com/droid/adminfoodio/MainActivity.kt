package com.droid.adminfoodio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivityLoginBinding
import com.droid.adminfoodio.databinding.ActivityMainBinding
import com.droid.adminfoodio.models.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completeOrderReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.addMenu.setOnClickListener {
            startActivity(Intent(this,AddItemActivity::class.java))
        }
        binding.viewMenu.setOnClickListener {
            startActivity(Intent(this,AllItemActivity::class.java))
        }
        binding.orderDispatch.setOnClickListener {
            startActivity(Intent(this,outForDeliveryActivity::class.java))
        }
        binding.viewProfile.setOnClickListener {
            startActivity(Intent(this,AdminProfileActivity::class.java))
        }
        binding.addNewUser.setOnClickListener {
            startActivity(Intent(this,AddUserActivity::class.java))
        }
        binding.logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        binding.pendingOrder.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity::class.java))
        }
        binding.textView8.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity::class.java))
        }

        pendingOrders()

        completeOrders()

        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {

        var listOfTotalPay= mutableListOf<Int>()
        completeOrderReference = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completeOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (orderSnapshot in snapshot.children){
                    var completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.totalPrice?.replace("$ ","")?.toIntOrNull()
                        ?.let { i->
                            listOfTotalPay.add(i)
                        }
                }
                binding.allTimeEarning.text="$ " + listOfTotalPay.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun completeOrders() {
        val completedOrderReference = database.reference.child("CompletedOrder")
        var completedOrderItemCount=0
        completedOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount=snapshot.childrenCount.toInt()
                binding.completedOrder.text=completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun pendingOrders() {
        database = FirebaseDatabase.getInstance()
        var pendingOrderReference = database.reference.child("OrderDetails")
        var pendingOrderItemCount=0
        pendingOrderReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount=snapshot.childrenCount.toInt()
                binding.pendingOrder.text=pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}