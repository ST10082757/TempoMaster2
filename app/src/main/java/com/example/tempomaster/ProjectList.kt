package com.example.tempomaster

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class ProjectList : AppCompatActivity() {

    private lateinit var goalsDatabase: DatabaseReference
    private lateinit var projectsDatabase: DatabaseReference
    private lateinit var barChart: BarChart
    private lateinit var progressBarChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)

        goalsDatabase = Firebase.database.reference.child("goals")
        projectsDatabase = Firebase.database.reference.child("projects")

        barChart = findViewById(R.id.barChart)
        progressBarChart = findViewById(R.id.progressBarChart)

        retrieveGoalData()
        retrieveProjectData()
    }

    private fun retrieveGoalData() {
        goalsDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val minGoalEntries = ArrayList<BarEntry>()
                val maxGoalEntries = ArrayList<BarEntry>()
                var index = 0f
                for (userSnapshot in dataSnapshot.children) {
                    for (goalSnapshot in userSnapshot.children) {
                        val goal = goalSnapshot.getValue(String::class.java)
                        val goalValues = goal?.split(" - ")?.map { it.toFloatOrNull() }
                        if (goalValues != null && goalValues.size == 2 && goalValues[0] != null && goalValues[1] != null) {
                            minGoalEntries.add(BarEntry(index, goalValues[0]!!))
                            maxGoalEntries.add(BarEntry(index, goalValues[1]!!))
                            index++
                        }
                    }
                }
                updateBarChart(minGoalEntries, maxGoalEntries)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateBarChart(
        minGoalEntries: ArrayList<BarEntry>,
        maxGoalEntries: ArrayList<BarEntry>
    ) {
        val minGoalDataSet = BarDataSet(minGoalEntries, "Min Goals").apply {
            color = Color.BLUE
            valueTextSize = 16f // Increase the text size on the bars
        }
        val maxGoalDataSet = BarDataSet(maxGoalEntries, "Max Goals").apply {
            color = Color.RED
            valueTextSize = 16f // Increase the text size on the bars
        }
        val data = BarData(minGoalDataSet, maxGoalDataSet).apply {
            barWidth = 0.3f // Set the width of the bars
        }
        barChart.data = data

        val xAxis = barChart.xAxis
        xAxis.textSize = 16f // Set the size of the labels

        barChart.invalidate()
    }

    private fun retrieveProjectData() {
        projectsDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val progressEntries = ArrayList<BarEntry>()
                var index = 0f
                for (snapshot in dataSnapshot.children) {
                    val startTime = snapshot.child("startTime").getValue(String::class.java)?.split(":")?.map { it.toIntOrNull() }
                    val endTime = snapshot.child("endTime").getValue(String::class.java)?.split(":")?.map { it.toIntOrNull() }
                    if (startTime != null && startTime.size == 2 && endTime != null && endTime.size == 2) {
                        val startHour = startTime[0]!!
                        val endHour = endTime[0]!!
                        val hoursWorked = if (endHour >= startHour) endHour - startHour else 24 - startHour + endHour
                        progressEntries.add(BarEntry(index, hoursWorked.toFloat()))
                        index++
                    }
                }
                updateProgressBarChart(progressEntries)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateProgressBarChart(progressEntries: ArrayList<BarEntry>) {
        val dataSet = BarDataSet(progressEntries, "Hours Worked").apply {
            color = Color.GREEN
            valueTextSize = 16f // Increase the text size on the bars
        }
        val data = BarData(dataSet).apply {
            barWidth = 0.3f // Set the width of the bars
        }
        progressBarChart.data = data
        progressBarChart.xAxis.textSize = 16f // Set the size of the labels
        progressBarChart.invalidate()
    }
}
