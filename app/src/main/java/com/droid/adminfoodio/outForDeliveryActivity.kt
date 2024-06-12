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

class outForDeliveryActivity : AppCompatActivity() {
    private  val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        enableEdgeToEdge()
        val customerNames= arrayListOf("Raju","Ramesh","Kaju","Katli","hridesh","vikas","Niks","chubham","black mamba","superman")
        val moneyStatus= arrayListOf("received","notReceived","Pending","received","notReceived","received","received","notReceived","received","received")
        val adapter=DeliveryAdapter(customerNames,moneyStatus)
        binding.outForDeliveryRv.adapter=adapter
        binding.outForDeliveryRv.layoutManager= LinearLayoutManager(this)

        //back button
        binding.imageButton.setOnClickListener {
            finish()
        }
    }
}