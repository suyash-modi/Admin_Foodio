package com.droid.adminfoodio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.adminfoodio.Adapters.OrderDetailsAdapter
import com.droid.adminfoodio.databinding.ActivityOrderDetailsBinding
import com.droid.adminfoodio.models.OrderDetails

class orderDetailsActivity : AppCompatActivity() {
    private  val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var userAddress: String? = null
    private var userPhone: String? = null
    private var totalPrice: String? = null

    private var foodName: ArrayList<String> = arrayListOf()
    private var foodPrice: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<Int> = arrayListOf()
    private var foodImage: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // back button
        binding.BackButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()

    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails

        receivedOrderDetails?.let {orderDetails ->

            userName=receivedOrderDetails.userName
            userAddress=receivedOrderDetails.address
            userPhone=receivedOrderDetails.phoneNumber
            totalPrice=receivedOrderDetails.totalPrice

            foodName=receivedOrderDetails.foodNames as ArrayList<String>
            foodPrice=receivedOrderDetails.foodPrices as ArrayList<String>
            foodQuantity=receivedOrderDetails.foodQuantities as ArrayList<Int>
            foodImage=receivedOrderDetails.foodImages as ArrayList<String>

            setUserDetails()

            setAdapter()

        }
    }


    private fun setUserDetails() {
        binding.Name.text = userName
        binding.Address.text = userAddress
        binding.phoneNo.text = userPhone
        binding.amount.text = "$ " + totalPrice

    }

    private fun setAdapter() {
        binding.odRv.layoutManager= LinearLayoutManager(this)
        binding.odRv.adapter= OrderDetailsAdapter(this,foodName,foodPrice,foodQuantity,foodImage)

    }
}