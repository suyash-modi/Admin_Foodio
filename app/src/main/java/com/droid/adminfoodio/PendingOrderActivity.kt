package com.droid.adminfoodio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        //initialize firebase
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()



        binding.imageButton.setOnClickListener {
            finish()
        }

    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
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
            }
        })
    }

    private fun addDataForListToRecyclerView() {
        for (orderItem: OrderDetails in listOfOrderItem) {

            // add data to respective list for recycler view
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }
                ?.forEach { listOfImageFirstFoodOrder.add(it) }
        }
        setAdapter()
    }

    private fun setAdapter() {
        binding.pendingOrderRv.layoutManager = LinearLayoutManager(this)
        binding.pendingOrderRv.adapter =
            PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImageFirstFoodOrder, this)


    }

    override fun onItemClicked(position: Int) {
        val intent = Intent(this, orderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListener(position: Int) {
        // handle item acceptance and update db
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val childItemRef =
            childItemPushKey?.let { database.reference.child("OrderDetails").child(it) }
        childItemRef?.child("orderAccepted")?.setValue(true)

        updateOrderAcceptedStatus(position)

    }

    private fun updateOrderAcceptedStatus(position: Int) {
        // update order acceptance in user buy history and order details
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val buyHistoryRef =
            database.reference.child("User").child(userIdOfClickedItem!!).child("BuyHistory")
                .child(pushKeyOfClickedItem!!)
        //val orderDetailsRef=pushKeyOfClickedItem?.let { database.reference.child("OrderDetails").child(it) }
        buyHistoryRef.child("orderAccepted").setValue(true)
        //orderDetailsRef?.child("orderAccepted")?.setValue(true)
        databaseOrderDetails.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)

    }

    override fun onItemDispatchClickListener(position: Int) {
        // handle item dispatch and update db

        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemRef =database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)

        dispatchItemRef.setValue(listOfOrderItem[position]).
                addOnSuccessListener {
                    deleteThisItemFromOrderData(dispatchItemPushKey)
                }


    }

    private fun deleteThisItemFromOrderData(dispatchItemPushKey: String){
        val deleteItemRef=database.reference.child("OrderDetails").child(dispatchItemPushKey)
        deleteItemRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Order is Not Dispatched", Toast.LENGTH_SHORT).show()
            }

    }
}