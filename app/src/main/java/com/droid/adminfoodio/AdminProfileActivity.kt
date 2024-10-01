package com.droid.adminfoodio

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivityAdminProfileBinding
import com.droid.adminfoodio.databinding.ActivityMainBinding
import com.droid.adminfoodio.models.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {

    private  val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        adminReference=database.reference.child("User")

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

        binding.saveBtn.setOnClickListener{
            val updateName=binding.Name.text.toString()
            val updateEmail=binding.email.text.toString()
            val updatePhone=binding.phone.text.toString()
            val updateAddress=binding.Address.text.toString()
            val updatePassword=binding.password.text.toString()

            updateUserData()
        }

        binding.imageButton.setOnClickListener {
            finish()
        }

        reteriveUserData()

    }

    private fun updateUserData() {
        val updateName=binding.Name.text.toString()
        val updateEmail=binding.email.text.toString()
        val updatePhone=binding.phone.text.toString()
        val updateAddress=binding.Address.text.toString()
        val updatePassword=binding.password.text.toString()

        val currentUserId=auth.currentUser?.uid
        if (currentUserId!=null){
           val userReference=adminReference.child(currentUserId)
            Toast.makeText(this, "Updated SuccessFully", Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("address").setValue(updateAddress)
            userReference.child("password").setValue(updatePassword)

        }

       else{

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reteriveUserData() {

        val currentUserId=auth.currentUser?.uid
        if (currentUserId!=null){

            val userReference=adminReference.child(currentUserId)

            userReference.addListenerForSingleValueEvent(object  : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val name=snapshot.child("name").getValue()
                        val email=snapshot.child("email").getValue()
                        val phone=snapshot.child("phone").getValue()
                        val address=snapshot.child("address").getValue()
                        val password=snapshot.child("password").getValue()

                        setDataToTextView(name,email,phone,address,password)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

    private fun setDataToTextView(
        name: Any?,
        email: Any?,
        phone: Any?,
        address: Any?,
        password: Any?
    ) {
        binding.Name.setText(name.toString())
        binding.email.setText(email.toString())
        binding.phone.setText(phone.toString())
        binding.Address.setText(address.toString())
        binding.password.setText(password.toString())
    }
}