package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.com.example.tempomaster.ProjectCategory
import com.example.tempomaster.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntentData()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> { /* Already in Dashboard */ }
                R.id.settingsID -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }
                R.id.projectID -> {
                    val intent = Intent(this, ProjectList::class.java)
                    startActivity(intent)
                }
            }
            true
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
