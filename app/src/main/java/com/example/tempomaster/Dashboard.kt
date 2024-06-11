package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.com.example.tempomaster.ProjectCategory
import com.example.tempomaster.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity(), View.OnClickListener {

    // Variables to store and hold click count
    private var workClickCount = 0
    private var schoolClickCount = 0
    private var generalClickCount = 0

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handling intent data
        handleIntentData()

        workClickCount = intent.getIntExtra("workClickCount", 0)
        schoolClickCount = intent.getIntExtra("schoolClickCount", 0)
        generalClickCount = intent.getIntExtra("generalClickCount", 0)

        // Passing the click count values
        binding.btnwork.text = "Work ($workClickCount)"
        binding.btnschool.text = "School ($schoolClickCount)"
        binding.btngeneral.text = "General ($generalClickCount)"

        // Setting click listeners for buttons
        binding.btnwork.setOnClickListener(this)
        binding.btnschool.setOnClickListener(this)
        binding.btngeneral.setOnClickListener(this)
        binding.btnschoolLogo.setOnClickListener(this)
        binding.btnworklogo.setOnClickListener(this)
        binding.btngeneralLogo.setOnClickListener(this)

        //project list button redirecting to view projects
        binding.projectBtn.setOnClickListener {
            val intent = Intent(this, ExistingProject::class.java)
            startActivity(intent)
        }


        // Game button that redirects to gamification
        binding.gameBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        // Navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settingsID -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    true
                }
                R.id.projectID -> {
                    val intent = Intent(this, ProjectList::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun handleIntentData() {
        intent.extras?.let {
            val projectName = it.getString("Project name")
            val startTime = it.getString("Start time")
            val endTime = it.getString("End time")

            // Check if the extras are not null before setting them to the TextViews
            if (projectName != null) {
                binding.txtProjectName.text = "Project name: $projectName"
            }
            if (startTime != null) {
                binding.txtProjectStartTime.text = "Start Time: $startTime"
            }
            if (endTime != null) {
                binding.txtProjectEndTime.text = "End Time: $endTime"
            }
        }
    }

    // Button that redirects user
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnwork -> {
                workClickCount++
                binding.btnwork.text = "Work ($workClickCount)"
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btnschool -> {
                schoolClickCount++
                binding.btnschool.text = "School ($schoolClickCount)"
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btngeneral -> {
                generalClickCount++
                binding.btngeneral.text = "General ($generalClickCount)"
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btnschoolLogo -> {
                schoolClickCount++
                val project = ProjectCategory("School")
                Toast.makeText(this@Dashboard, "You have added a new School project", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddProject::class.java)
                startActivity(intent)
            }
            R.id.btnworklogo -> {
                workClickCount++
                val project = ProjectCategory("Work")
                Toast.makeText(this@Dashboard, "You have added a new Work project", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddProject::class.java)
                startActivity(intent)
            }
            R.id.btngeneralLogo -> {
                generalClickCount++
                val project = ProjectCategory("General")
                Toast.makeText(this@Dashboard, "You have added a new General project", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dashboard::class.java)
                intent.putExtra("workClickCount", workClickCount)
                intent.putExtra("schoolClickCount", schoolClickCount)
                intent.putExtra("generalClickCount", generalClickCount)
                startActivity(intent)
            }
        }
    }
}
