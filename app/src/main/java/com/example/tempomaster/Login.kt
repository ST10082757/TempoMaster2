package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tempomaster.databinding.ActivityLoginBinding
import com.example.tempomaster.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class
Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val backbtn = findViewById<Button>(R.id.btnBack)
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }

        binding.linkSignup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val email = binding.txtUsername.text.toString()
            val pass = binding.txtPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, Welcome::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Authentication failed: ${it.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to it after login

        }
    }
}


