package com.droid.adminfoodio

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivitySignUpBinding
import com.droid.adminfoodio.models.userModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email : String
    private lateinit var Password: String
    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        // initialize Firebase Auth
        auth = Firebase.auth
// initialize Firebase database
        database = Firebase.database.reference

        binding.createAccount.setOnClickListener {
            email = binding.emailOrPhone.text.toString().trim()
            Password = binding.password.text.toString().trim()
            userName = binding.nameOfOwner.text.toString().trim()
            nameOfRestaurant = binding.nameOfRestaurent.text.toString().trim()
            
            if (email.isNotEmpty() && Password.isNotEmpty() && userName.isNotEmpty() && nameOfRestaurant.isNotEmpty()) {
                createAccount(email, Password,userName,nameOfRestaurant)
            }
            else{
                Toast.makeText(this, "please fill all details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.Alreadybtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        val locationList=arrayOf("Jhansi","Delhi","Lucknow","Gonda","Gorukhpur","Agra")
        val adapter= ArrayAdapter(this, R.layout.simple_list_item_1,locationList)
        binding.listOfLocations.setAdapter(adapter)


    }

    private fun createAccount(
        email: String,
        password: String,
        userName: String,
        nameOfRestaurant: String
    ) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Account created successfully please login", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: faliure",it.exception)
            }
        }
    }

    private fun saveUserData() {
        email = binding.emailOrPhone.text.toString().trim()
        Password = binding.password.text.toString().trim()
        userName = binding.nameOfOwner.text.toString().trim()
        nameOfRestaurant = binding.nameOfRestaurent.text.toString().trim()

        val user= userModel (userName, nameOfRestaurant, email, Password)
        val userId : String = FirebaseAuth.getInstance().currentUser!!.uid
        database.child(  "User").child(userId).setValue(user)
    }
}