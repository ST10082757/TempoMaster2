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


                    val bundle = Bundle()
                    bundle.putString("Date", date)
                    bundle.putString("Project Name", projectName)
                    bundle.putString("Description", description)
                    bundle.putString("Start Time", startTime)
                    bundle.putString("End Time", endTime)

                    intentHelper.startExistingProjectActivity(this, Dashboard::class.java, bundle)
                } else {
                    Toast.makeText(this, "Date is null", Toast.LENGTH_SHORT).show()
                }
            }
        }}}