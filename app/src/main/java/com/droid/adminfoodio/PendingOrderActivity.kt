package com.droid.adminfoodio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.adminfoodio.Adapters.PendingOrderAdapter
import com.droid.adminfoodio.databinding.ActivityPendingOrderBinding
import com.droid.adminfoodio.models.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity() {
    private  val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    private var listOfName:MutableList<String> = mutableListOf()
    private var listOfTotalPrice:MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listOfOrderItem:MutableList<OrderDetails> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        //initialize firebase
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()



        binding.imageButton.setOnClickListener{
            finish()
        }

    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderDetails::class.java)
                    order?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataForListToRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addDataForListToRecyclerView() {
        for(orderItem : OrderDetails in listOfOrderItem){

            // add data to respective list for recycler view
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach { listOfImageFirstFoodOrder.add(it) }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRv.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRv.adapter = PendingOrderAdapter(this,listOfName,listOfTotalPrice,listOfImageFirstFoodOrder)

    }
}