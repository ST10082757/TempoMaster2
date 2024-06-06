package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.com.example.tempomaster.ProjectCategory
import com.example.tempomaster.com.example.tempomaster.openIntent
import com.example.tempomaster.databinding.ActivityWelcomeBinding


class Welcome : AppCompatActivity() , View.OnClickListener {

    //creating an object for project category class
    var project = ProjectCategory()

    //declaring a counter variable
    var clickCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        //setting the layout
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //binding the welcome page with the welcome screen
        val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adding click listeners to the buttons
        binding.workbtn.setOnClickListener(this)
        binding.schoolbtn.setOnClickListener(this)
        binding.generalbtn.setOnClickListener(this)

        //setting the button to a listener and redirecting user to next page
        binding.btnNext.setOnClickListener {
            if (clickCount == 0) {
                // Show a toast indicating the user has not made a selection
                Toast.makeText(this@Welcome, "Please select a category", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, Dashboard::class.java)
                intent.putExtra("workClickCount", if (project.projectCategory == "Work") 1 else 0)
                intent.putExtra("schoolClickCount", if (project.projectCategory == "School") 1 else 0)
                intent.putExtra("generalClickCount", if (project.projectCategory == "General") 1 else 0)
                startActivity(intent)
            }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.workbtn -> {
                project.projectCategory = "Work"
                this.clickCount++
                Toast.makeText(this@Welcome, "You have selected: Work", Toast.LENGTH_SHORT).show()
            }

            R.id.schoolbtn -> {
                project.projectCategory = "School"
                this.clickCount++
                Toast.makeText(this@Welcome, "You have selected: School", Toast.LENGTH_SHORT).show()
            }

            R.id.generalbtn -> {
                project.projectCategory = "General"
                this.clickCount++
                Toast.makeText(this@Welcome, "You have selected: General", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}