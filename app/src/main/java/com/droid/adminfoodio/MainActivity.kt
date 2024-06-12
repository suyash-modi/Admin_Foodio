package com.droid.adminfoodio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivityLoginBinding
import com.droid.adminfoodio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
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

        }
        binding.pendingOrder.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity::class.java))
        }
    }
}