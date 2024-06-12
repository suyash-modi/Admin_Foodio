package com.droid.adminfoodio

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivityAdminProfileBinding
import com.droid.adminfoodio.databinding.ActivityMainBinding

class AdminProfileActivity : AppCompatActivity() {

    private  val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        binding.Name.isEnabled=false
        binding.email.isEnabled=false
        binding.phone.isEnabled=false
        binding.Address.isEnabled=false
        binding.password.isEnabled=false

        var isEnable = false
        binding.editBtn.setOnClickListener {
            isEnable=!isEnable
            binding.Name.isEnabled=isEnable
            binding.email.isEnabled=isEnable
            binding.phone.isEnabled=isEnable
            binding.Address.isEnabled=isEnable
            binding.password.isEnabled=isEnable
        }
        if (isEnable) {
            binding.Name.requestFocus()
        }

        binding.imageButton.setOnClickListener {
            finish()
        }

    }
}