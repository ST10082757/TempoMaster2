package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.com.example.tempomaster.ProjectCategory
import com.example.tempomaster.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDashboardBinding

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


    private fun handleIntentData() {
        intent.extras?.let {
            val projectName = it.getString("Project Name")
            val startTime = it.getString("Start Time")
            val endTime = it.getString("End Time")

            binding.txtProjectName.text = "Project name: $projectName"
            binding.txtProjectStartTime.text = "Start Time: $startTime"
            binding.txtProjectEndTime.text = "End Time: $endTime"


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
