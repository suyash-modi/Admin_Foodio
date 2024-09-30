package com.droid.adminfoodio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.adminfoodio.Adapters.DeliveryAdapter
import com.droid.adminfoodio.databinding.ActivityMainBinding
import com.droid.adminfoodio.databinding.ActivityOutForDeliveryBinding
import com.droid.adminfoodio.models.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class outForDeliveryActivity : AppCompatActivity() {
    private  val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderDetails: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        //back button
        binding.imageButton.setOnClickListener {
            finish()
        }

        retrieveCompleteOrderDetails()
    }

    private fun retrieveCompleteOrderDetails() {
        database=FirebaseDatabase.getInstance()
        val completeOrderDetailsRef=database.reference.child("CompletedOrder").orderByChild("currentTime")

        completeOrderDetailsRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderDetails.clear()

                for (orderSnapshot in snapshot.children){
                    val completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderDetails.add(it)
                    }
                }
                listOfCompleteOrderDetails.reverse()

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setDataIntoRecyclerView() {
        val costumerName= mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()

        for(orderDetails in listOfCompleteOrderDetails){
            orderDetails.userName?.let { costumerName.add(it) }
            moneyStatus.add(orderDetails.paymentReceived)
        }

        val adapter=DeliveryAdapter(costumerName,moneyStatus)
        binding.outForDeliveryRv.layoutManager=LinearLayoutManager(this)
        binding.outForDeliveryRv.adapter=adapter
    }
}