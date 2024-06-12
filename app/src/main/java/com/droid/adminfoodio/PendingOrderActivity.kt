package com.droid.adminfoodio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.droid.adminfoodio.Adapters.PendingOrderAdapter
import com.droid.adminfoodio.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {
    private  val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        enableEdgeToEdge()
        binding.imageButton.setOnClickListener{
            finish()
        }

        val foodName= listOf("Burger","Sandwich","Momos","Garlic Bread","cheesy dip")
        val price= listOf("7","12","17","23","3")
        val images= listOf(
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto,
            R.drawable.photomenu,
            R.drawable.menuphoto)

        val adapter= PendingOrderAdapter(ArrayList(foodName),ArrayList(price),ArrayList(images),this)
        binding.pendingOrderRv.layoutManager= LinearLayoutManager(this)
        binding.pendingOrderRv.adapter=adapter

    }
}