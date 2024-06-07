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

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Set click listeners for buttons
        findViewById<Button>(R.id.btnwork).setOnClickListener {
            startActivity(Intent(this, ExistingProject::class.java))
        }

        findViewById<Button>(R.id.btnschool).setOnClickListener {
            startActivity(Intent(this, ExistingProject::class.java))
        }


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


    //method that gets data from add project and displays on recent project card view

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
    //button that redirects user
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnwork ->
                {
                binding.btnwork.text = "Work ($workClickCount)"
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btnschool ->
                {
                binding.btnschool.text = "School ($schoolClickCount)"
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }
            R.id.btngeneral ->
                {
                binding.btngeneral.text = "General ($generalClickCount)"
                val intent = Intent(this, ExistingProject::class.java)
                startActivity(intent)
            }

            R.id.btnschoolLogo ->
                {
                Toast.makeText(this@Dashboard, "You have selected to add a new School project", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddProject::class.java)
                startActivity(intent)
            }
            R.id.btnworklogo ->
                {
                Toast.makeText(this@Dashboard, "You have selected to add a new Work project", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddProject::class.java)
                startActivity(intent)
            }
            R.id.btngeneralLogo ->
                {
                Toast.makeText(this@Dashboard, "You have selected to add a new General project", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddProject::class.java)
                startActivity(intent)
            }
        }

    }
}
