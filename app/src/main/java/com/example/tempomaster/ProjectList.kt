package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProjectList : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)

        database = Firebase.database.reference.child("goals")
        pieChart = findViewById(R.id.pieChart)

        retrieveData()

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
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pieEntries = ArrayList<PieEntry>()
                for (userSnapshot in dataSnapshot.children) {
                    for (goalSnapshot in userSnapshot.children) {
                        val goal = goalSnapshot.getValue(String::class.java)
                        if (goal != null) {
                            try {
                                val goalValue = goal.split(" - ")[1].toFloat() // get the second part of the goal string and convert it to Float
                                pieEntries.add(PieEntry(goalValue))
                            } catch (e: NumberFormatException) {
                                // Handle the case where goal cannot be converted to Float
                            }
                        }
                    }
                }
                updatePieChart(pieEntries)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ProjectList, "Failed to load goals.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updatePieChart(pieEntries: ArrayList<PieEntry>) {
        val dataSet = PieDataSet(pieEntries, "Goals")
        dataSet.valueTextSize = 16f // Set the text size to 16dp
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()
    }
}