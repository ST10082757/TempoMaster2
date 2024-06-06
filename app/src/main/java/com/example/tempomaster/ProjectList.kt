package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProjectList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_project_list)

        // Resetting start and due dates when the delete button is clicked
        val deleteButton = findViewById<Button>(R.id.deleteProject)
        val startDateValue = findViewById<TextView>(R.id.startDateValue)
        val dueDateValue = findViewById<TextView>(R.id.dueDateValue)
        val projectDescription = findViewById<EditText>(R.id.projectDescription)

        deleteButton.setOnClickListener{
            startDateValue.text =""
            dueDateValue.text = ""
            projectDescription.text = null
            // displays the text after the user click the button
            Toast.makeText(this,"Project deleted!", Toast.LENGTH_SHORT).show()
        }

        //---------------------Bottom navigation------------------------//
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }
                R.id.settingsID -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }
                else -> false
            }
            true // Return true to indicate successful handling
        }
    }
}