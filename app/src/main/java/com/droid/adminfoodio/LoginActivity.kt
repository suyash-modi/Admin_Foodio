package com.droid.adminfoodio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.droid.adminfoodio.databinding.ActivityLoginBinding
import com.droid.adminfoodio.models.userModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private var userName: String? = null
    private var nameOfRestaurant: String? = null
    private lateinit var Password: String
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        enableEdgeToEdge()
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        // initialize Firebase Auth
        auth = Firebase.auth
        // initialize Firebase database
        database = Firebase.database.reference
        // initialize google Sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.dontbtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginbtn.setOnClickListener {
            email = binding.emailAndPhone.text.toString().trim()
            Password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && Password.isNotEmpty()) {
                craeteUserAccount(email, Password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.googleBtn.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            launcher.launch(intent)
        }

    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun saveUserData() {
        // get text form edit text
        email = binding.emailAndPhone.text.toString().trim()
        Password = binding.password.text.toString().trim()
        val user = userModel(userName, nameOfRestaurant, email, Password)
        val userId: String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            database.child("User").child(it).setValue(user)
        }
    }

    private fun craeteUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        saveUserData()
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        Log.d(
                            "Account",
                            "createUser Account: Authentication failed",
                            task.exception
                        )
                    }
                }
            }
        }
    }

    private val launcher= registerForActivityResult(ActivityResultContracts. StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val account: GoogleSignInAccount = task.result
                val credential: AuthCredential =
                    GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{ authTask ->
                    if (authTask.isSuccessful) {
                      // successfully sign in with Google
                        Toast.makeText(this, "Successfully sign-in with Google", Toast.LENGTH_SHORT).show()
                        updateUi(authTask.result?.user)
                        finish()
                    } else {
                        Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Google Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent( this, MainActivity::class.java))
            finish()
        }
    }
}