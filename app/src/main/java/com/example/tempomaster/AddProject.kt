package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.databinding.ActivityAddProjectBinding
import java.text.SimpleDateFormat
import java.util.*

class AddProject : AppCompatActivity() {
    private lateinit var calendarView: CalendarView
    private lateinit var binding: ActivityAddProjectBinding
    private val intentHelper = TheIntentHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarView = findViewById(R.id.projectCalendar)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val dateSelected = "$dayOfMonth-${month + 1}-$year"

            val projectNameInput = findViewById<EditText>(R.id.AddProjName)
            val descriptionInput = findViewById<EditText>(R.id.Descriptiontxt)
            val startTimeInput = findViewById<EditText>(R.id.txtstartTime)
            val endTimeInput = findViewById<EditText>(R.id.txtEndTime)

            val clickToAddProj = findViewById<Button>(R.id.clickAddPrj)
            clickToAddProj.setOnClickListener {
                val date: String? = dateSelected
                if (date != null) {
                    val projectName = projectNameInput.text.toString()
                    val description = descriptionInput.text.toString()
                    val startTime = startTimeInput.text.toString()
                    val endTime = endTimeInput.text.toString()

                    if (validateInputs(projectName, description, startTime, endTime)) {
                        val timeLeft = calculateTimeLeft(startTime, endTime)

                        val bundle = Bundle()
                        bundle.putString("Date", date)
                        bundle.putString("Project Name", projectName)
                        bundle.putString("Description", description)
                        bundle.putString("Start Time", startTime)
                        bundle.putString("End Time", endTime)
                        bundle.putString("Time Left", timeLeft)

                        intentHelper.startExistingProjectActivity(this, ExistingProject::class.java, bundle)
                    }
                } else {
                    Toast.makeText(this, "Date is null", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(
        projectName: String,
        description: String,
        startTime: String,
        endTime: String
    ): Boolean {
        if (projectName.isBlank() || description.isBlank() || startTime.isBlank() || endTime.isBlank()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun calculateTimeLeft(startTime: String, endTime: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val start = sdf.parse(startTime)
        val end = sdf.parse(endTime)
        val diffInMillis = end.time - start.time

        val hours = (diffInMillis / (1000 * 60 * 60)).toInt()
        val minutes = (diffInMillis / (1000 * 60) % 60).toInt()

        return "$hours hours $minutes minutes"
    }
}
