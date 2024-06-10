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

    private lateinit var database: DatabaseReference
    private lateinit var barChart: BarChart
    private lateinit var progressBarChart: BarChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)

        database = Firebase.database.reference.child("goals")
        barChart = findViewById(R.id.barChart)
        progressBarChart = findViewById(R.id.progressBarChart)

        retrieveData()
        updateProgressBarChart()
    }

    private fun retrieveData() {
        database.addValueEventListener(object : ValueEventListener {
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
        }
        val maxGoalDataSet = BarDataSet(maxGoalEntries, "Max Goals").apply {
            color = Color.RED
        }
        val data = BarData(minGoalDataSet, maxGoalDataSet).apply {
            barWidth = 0.3f // Set the width of the bars
        }
        barChart.data = data

        val xAxis = barChart.xAxis
        xAxis.textSize = 16f // Set the size of the labels

        barChart.invalidate()
    }
    private fun updateProgressBarChart() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1) // Get the date one month ago
        val oneMonthAgo = calendar.timeInMillis

        database.orderByChild("timestamp").startAt(oneMonthAgo.toDouble()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val progressEntries = ArrayList<BarEntry>()
                var index = 0f
                for (snapshot in dataSnapshot.children) {
                    val goal = snapshot.getValue(Goal::class.java)
                    if (goal != null) {
                        val yValue = if (goal.hours >= goal.minGoal && goal.hours <= goal.maxGoal) 1f else 0f
                        progressEntries.add(BarEntry(index, yValue))
                        index++
                    }
                }
                val dataSet = BarDataSet(progressEntries, "Goal Progress").apply {
                    color = Color.GREEN
                }
                val data = BarData(dataSet).apply {
                    barWidth = 0.3f // Set the width of the bars
                }
                progressBarChart.data = data
                progressBarChart.invalidate()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}

data class Goal(
    val timestamp: Long,
    val hours: Float,
    val minGoal: Float,
    val maxGoal: Float
)