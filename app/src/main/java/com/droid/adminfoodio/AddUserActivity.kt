package com.droid.adminfoodio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivityAddUserBinding
import com.droid.adminfoodio.databinding.ActivityAllItemBinding

class AddUserActivity : AppCompatActivity() {
    private  val binding: ActivityAddUserBinding by lazy {
        ActivityAddUserBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        enableEdgeToEdge()
        binding.loginbtn.setOnClickListener {
            finish()
        }
    }
}