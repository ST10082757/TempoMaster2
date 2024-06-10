package com.example.tempomaster

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.databinding.ActivityDashboardBinding
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
    private lateinit var selectTimePeriodButton: Button
    private lateinit var undoButton: Button

    private var selectedStartTime: String = "00:00"
    private var selectedEndTime: String = "23:59"
    private var originalProgressEntries = ArrayList<BarEntry>()
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)

        //-------------------------Navigation bar----------------------//
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

        goalsDatabase = Firebase.database.reference.child("goals")
        projectsDatabase = Firebase.database.reference.child("projects")

        barChart = findViewById(R.id.barChart)
        progressBarChart = findViewById(R.id.progressBarChart)
        selectTimePeriodButton = findViewById(R.id.selectTimePeriodButton)
        undoButton = findViewById(R.id.undoButton)

        selectTimePeriodButton.setOnClickListener {
            showTimePickerDialog()
        }

        undoButton.setOnClickListener {
            resetFilters()
        }

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
                        val startMinute = startTime[1]!!
                        val endHour = endTime[0]!!
                        val endMinute = endTime[1]!!
                        val selectedStartHour = selectedStartTime.split(":")[0].toInt()
                        val selectedStartMinute = selectedStartTime.split(":")[1].toInt()
                        val selectedEndHour = selectedEndTime.split(":")[0].toInt()
                        val selectedEndMinute = selectedEndTime.split(":")[1].toInt()

                        if (isTimeWithinSelectedPeriod(startHour, startMinute, endHour, endMinute, selectedStartHour, selectedStartMinute, selectedEndHour, selectedEndMinute)) {
                            val hoursWorked = if (endHour >= startHour) endHour - startHour else 24 - startHour + endHour
                            progressEntries.add(BarEntry(index, hoursWorked.toFloat()))
                            index++
                        } else {
                            val hoursWorked = if (endHour >= startHour) endHour - startHour else 24 - startHour + endHour
                            originalProgressEntries.add(BarEntry(index, hoursWorked.toFloat()))
                            index++
                        }
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

    private fun isTimeWithinSelectedPeriod(
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        selectedStartHour: Int,
        selectedStartMinute: Int,
        selectedEndHour: Int,
        selectedEndMinute: Int
    ): Boolean {
        val startTimeInMinutes = startHour * 60 + startMinute
        val endTimeInMinutes = endHour * 60 + endMinute
        val selectedStartTimeInMinutes = selectedStartHour * 60 + selectedStartMinute
        val selectedEndTimeInMinutes = selectedEndHour * 60 + selectedEndMinute

        return startTimeInMinutes >= selectedStartTimeInMinutes && endTimeInMinutes <= selectedEndTimeInMinutes
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val startTimePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
            selectedStartTime = String.format("%02d:%02d", hourOfDay, minute)
            val endTimePicker = TimePickerDialog(this, { _, endHour, endMinute ->
                selectedEndTime = String.format("%02d:%02d", endHour, endMinute)
                retrieveProjectData()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            endTimePicker.show()
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        startTimePicker.show()
    }

    private fun resetFilters() {
        selectedStartTime = "00:00"
        selectedEndTime = "23:59"
        updateProgressBarChart(originalProgressEntries)
    }
}
