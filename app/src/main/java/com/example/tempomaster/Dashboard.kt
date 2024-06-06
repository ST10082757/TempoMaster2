package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.com.example.tempomaster.ProjectCategory
import com.example.tempomaster.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Dashboard : AppCompatActivity(), View.OnClickListener {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    //testing
    var projectCategory : String = ""

    var workClickCount = 0
    var schoolClickCount = 0
    var generalClickCount = 0

    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Retrieve selected project category from Intent extras
        projectCategory = intent.getStringExtra("projectCategory") ?: ""

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        workClickCount = intent.getIntExtra("workClickCount", 0)
        schoolClickCount = intent.getIntExtra("schoolClickCount", 0)
        generalClickCount = intent.getIntExtra("generalClickCount", 0)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnwork.text = "Work ($workClickCount)"
        binding.btnschool.text = "School ($schoolClickCount)"
        binding.btngeneral.text = "General ($generalClickCount)"
        binding.btnworklogo.text = "Work ($workClickCount)"
        binding.btngeneralLogo.text = "General ($generalClickCount)"
        binding.btnschoolLogo.text = "School ($schoolClickCount)"

        binding.btnwork.setOnClickListener(this)
        binding.btnschool.setOnClickListener(this)
        binding.btngeneral.setOnClickListener(this)
        binding.btnworklogo.setOnClickListener(this)
        binding.btnschoolLogo.setOnClickListener(this)
        binding.btngeneralLogo.setOnClickListener(this)

        binding.gameBtn.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val projectName = binding.txtProjectName.text.toString()
            val projectTimeSpent = binding.txtProjectTimeSpent.text.toString()
            val projectTimeLeft = binding.txtProjectTimeLeft.text.toString()

            if (projectName.isNotEmpty() && projectTimeSpent.isNotEmpty() && projectTimeLeft.isNotEmpty()) {
                saveProjectToDatabase(projectName, projectTimeSpent, projectTimeLeft)
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }

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
                else -> false
            }
            true
        }

        readProjectsFromDatabase()
    }

    private fun saveProjectToDatabase(name: String, timeSpent: String, timeLeft: String) {
        val userId = firebaseAuth.currentUser?.uid
        val project = ProjectCategory(name, timeSpent, timeLeft)

        if (userId != null) {
            database.child("users").child(userId).child("projects").push()
                .setValue(project)
                .addOnSuccessListener {
                    Toast.makeText(this, "Project details successfully entered", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddProject::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding project: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readProjectsFromDatabase() {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            database.child("users").child(userId).child("projects")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val projects = mutableListOf<ProjectCategory>()
                        for (projectSnapshot in snapshot.children) {
                            val project = projectSnapshot.getValue(ProjectCategory::class.java)
                            project?.let { projects.add(it) }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@Dashboard, "Error reading projects: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

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
