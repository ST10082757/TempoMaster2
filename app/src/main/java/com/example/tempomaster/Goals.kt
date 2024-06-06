package com.example.tempomaster

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.databinding.ActivityGoalsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class Goals : AppCompatActivity() {
    private lateinit var minGoalEditText: EditText
    private lateinit var maxGoalEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button
    private lateinit var listOfGoals: ListView
    private lateinit var goalAdapter: GoalAdapter
    private lateinit var binding: ActivityGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        minGoalEditText = findViewById(R.id.minGoal)
        maxGoalEditText = findViewById(R.id.maxGoal)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)
        listOfGoals = findViewById(R.id.ListOfGoals)

        goalAdapter = GoalAdapter(this)
        listOfGoals.adapter = goalAdapter

        saveButton.setOnClickListener {
            val minGoal = minGoalEditText.text.toString()
            val maxGoal = maxGoalEditText.text.toString()

            if (minGoal.isNotBlank() && maxGoal.isNotBlank() && minGoal.isNumeric() && maxGoal.isNumeric()) {
                goalAdapter.addProject("$minGoal - $maxGoal")
                minGoalEditText.text.clear()
                maxGoalEditText.text.clear()
            } else {
                // Show a toast message if input is invalid
                Toast.makeText(this, "Please enter valid minimum and maximum goals", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> {
                    // Already in Dashboard
                    true
                }
                R.id.settingsID -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                    true
                }
                R.id.projectID -> {
                    val intent = Intent(this, ExistingProject::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    // Inner class for the GoalAdapter
    inner class GoalAdapter(context: Context) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {
        // Function to add a new project to the list
        fun addProject(project: String) {
            add(project)
            notifyDataSetChanged()
        }
    }

    private fun String.isNumeric(): Boolean {
        return this.matches("-?\\d+(\\.\\d+)?".toRegex())
    }
}
