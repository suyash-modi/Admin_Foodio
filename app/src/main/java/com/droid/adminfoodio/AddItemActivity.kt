package com.droid.adminfoodio

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.droid.adminfoodio.databinding.ActivityAddItemBinding
import com.droid.adminfoodio.models.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddItemActivity : AppCompatActivity() {

    // Item details
    private lateinit var foodName: String
    private lateinit var foodDescription: String
    private lateinit var foodPrice: String
    private var foodImageUri: Uri? = null
    private lateinit var foodIngredient: String

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.addItemBtn.setOnClickListener {
            foodName = binding.itemName.text.toString()
            foodDescription = binding.shortDescription.text.toString()
            foodPrice = binding.itemPrice.text.toString()
            foodIngredient = binding.ingredients.text.toString()

            if (foodName.isNotEmpty() && foodDescription.isNotEmpty() && foodPrice.isNotEmpty() && foodIngredient.isNotEmpty()) {
                uploadData()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.addImagebtn.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.cardView.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.imageButton.setOnClickListener {
            finish()
        }

    }

    private fun uploadData() {
        val menuRef: DatabaseReference = database.getReference("menu")
        val newItemKey: String? = menuRef.push().key

        if (newItemKey != null && foodImageUri != null) {
            val storageRef: StorageReference = FirebaseStorage.getInstance().reference
            val imageRef: StorageReference = storageRef.child("menu_images/$newItemKey.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredient,
                        foodImage = downloadUrl.toString()
                    )

                    menuRef.child(newItemKey).setValue(newItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.itemImage.setImageURI(it)
            foodImageUri = it
        }
    }
}
