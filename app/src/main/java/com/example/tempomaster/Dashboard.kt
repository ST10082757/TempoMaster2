package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.com.example.tempomaster.ProjectCategory
import com.example.tempomaster.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Dashboard : AppCompatActivity(), View.OnClickListener {
   // private lateinit var firebaseAuth: FirebaseAuth
   // private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityDashboardBinding

    class Dashboard : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_dashboard)

            // Set click listeners for buttons
            findViewById<Button>(R.id.btnwork).setOnClickListener {
                startActivity(Intent(this, ExistingProject::class.java))
            }

            findViewById<Button>(R.id.btnschool).setOnClickListener {
                startActivity(Intent(this, ExistingProject::class.java))
            }

            findViewById<Button>(R.id.btngeneral).setOnClickListener {
                startActivity(Intent(this, ExistingProject::class.java))
            }

            findViewById<Button>(R.id.btnworklogo).setOnClickListener {
                startActivity(Intent(this, AddProject::class.java))
            }

            findViewById<Button>(R.id.btnschoolLogo).setOnClickListener {
                startActivity(Intent(this, AddProject::class.java))
            }

            findViewById<Button>(R.id.btngeneralLogo).setOnClickListener {
                startActivity(Intent(this, AddProject::class.java))
            }
        }
    }

    private fun handleIntentData() {
        intent.extras?.let {
            val projectName = it.getString("ProjectName")
            val timeLeft = it.getString("TimeLeft")

            binding.txtProjectName.text = projectName
            binding.txtProjectTimeLeft.text = "Time Left: $timeLeft"
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnwork -> {
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btnschool -> {
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btngeneral -> {
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btnschoolLogo, R.id.btnworklogo, R.id.btngeneralLogo -> {
                val intent = Intent(this, AddProject::class.java)
                startActivity(intent)
            }
        }
    }
}
