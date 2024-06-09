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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.github.mikephil.charting.data.Entry
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import com.google.firebase.database.DatabaseError
import android.graphics.Color



class ProjectList : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_project_list)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("goals")
        pieChart = findViewById(R.id.pieChart)

        // Resetting start and due dates when the delete button is clicked
        val deleteButton = findViewById<Button>(R.id.deleteProject)
        val startDateValue = findViewById<TextView>(R.id.startDateValue)
        val dueDateValue = findViewById<TextView>(R.id.dueDateValue)
        val projectDescription = findViewById<EditText>(R.id.projectDescription)

        deleteButton.setOnClickListener {
            startDateValue.text = ""
            dueDateValue.text = ""
            projectDescription.text = null
            // displays the text after the user click the button
            Toast.makeText(this, "Project deleted!", Toast.LENGTH_SHORT).show()
        }
        // Populate the pie chart with dummy data for demonstration
        retrieveData()
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

    private fun retrieveData() {
        databaseReference.child("goals").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pieEntries = ArrayList<PieEntry>()
                if (dataSnapshot.hasChildren()) {
                    for (goalSnapshot in dataSnapshot.children) {
                        // Get the start and end times for each goal
                        val startTime = goalSnapshot.child("startTime").getValue(String::class.java)
                        val endTime = goalSnapshot.child("endTime").getValue(String::class.java)

                        // Assuming startTime and endTime are not null
                        if (startTime != null && endTime != null) {
                            // Calculate the duration of each goal
                            val duration = calculateDuration(startTime, endTime)
                            pieEntries.add(PieEntry(duration.toFloat())) // Convert duration to Float
                        }
                    }
                    updatePieChart(pieEntries)
                } else {
                    pieChart.clear()
                    pieChart.invalidate()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancelled event
            }
        })
    }
    private fun calculateDuration(startTime: String, endTime: String): Long {
        val pattern = "HH:mm:ss"
        val formatter = SimpleDateFormat(pattern)

        // Parse start time and end time strings to Date objects
        val startDate = formatter.parse(startTime)
        val endDate = formatter.parse(endTime)

        // Calculate the difference in milliseconds between end time and start time
        val difference = endDate.time - startDate.time

        // Convert milliseconds to hours
        val hours = TimeUnit.MILLISECONDS.toHours(difference)

        return hours
    }

    private fun updatePieChart(pieEntries: ArrayList<PieEntry>) {
        val dataSet = PieDataSet(pieEntries, "Pie Chart Data")
        dataSet.colors = mutableListOf(Color.BLUE, Color.GREEN, Color.RED) // Example colors
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }
}