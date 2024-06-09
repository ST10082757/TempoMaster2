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

    //variables to store and hold click count
    var workClickCount = 0
    var schoolClickCount = 0
    var generalClickCount = 0

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        //instantiating intent class
        handleIntentData()

        workClickCount = intent.getIntExtra("workClickCount", 0)
        schoolClickCount = intent.getIntExtra("schoolClickCount", 0)
        generalClickCount = intent.getIntExtra("generalClickCount", 0)

        //passing the click count values
        binding.btnwork.text = "Work ($workClickCount)"
        binding.btnschool.text = "School ($schoolClickCount)"
        binding.btngeneral.text = "General ($generalClickCount)"

        // setting click listeners for buttons
        binding.btnwork.setOnClickListener(this)
        binding.btnschool.setOnClickListener(this)
        binding.btngeneral.setOnClickListener(this)
        binding.btnschoolLogo.setOnClickListener(this)
        binding.btnworklogo.setOnClickListener(this)
        binding.btngeneralLogo.setOnClickListener(this)

        //game button that redirects to gamification
        binding.gameBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        //navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }

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

    //button that redirects user
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
            R.id.btnschoolLogo, R.id.btnworklogo, R.id.btngeneralLogo -> {
                val intent = Intent(this, AddProject::class.java)
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
