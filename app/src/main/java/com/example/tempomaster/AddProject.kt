package com.example.tempomaster

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.databinding.ActivityAddProjectBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddProject : AppCompatActivity() {
    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var databaseReference: DatabaseReference
    private val intentHelper = TheIntentHelper()
    private var dateSelected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("projects")

        // Set up the CalendarView
        binding.projectCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            dateSelected = "$dayOfMonth-${month + 1}-$year"
        }

        // Set up the Time pickers
        binding.txtstartTime.setOnClickListener { showTimePicker { time -> binding.txtstartTime.setText(time) } }
        binding.txtEndTime.setOnClickListener { showTimePicker { time -> binding.txtEndTime.setText(time) } }

        // Add project button
        binding.clickAddPrj.setOnClickListener {
            val projectName = binding.AddProjName.text.toString()
            val description = binding.Descriptiontxt.text.toString()
            val startTime = binding.txtstartTime.text.toString()
            val endTime = binding.txtEndTime.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (projectName.isNotEmpty() && description.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && category.isNotEmpty() && dateSelected.isNotEmpty()) {
                val project = Project(projectName, description, dateSelected, startTime, endTime, category)
                databaseReference.push().setValue(project)
                    .addOnSuccessListener {
                        Toast.makeText(this@AddProject, "Project added successfully", Toast.LENGTH_SHORT).show()
                        clearFields()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@AddProject, "Failed to add project", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Back button
        binding.backclick.setOnClickListener {
            intentHelper.goBack(this)
        }

        // Camera button
        binding.cameraBtn.setOnClickListener {
            intentHelper.startCameraActivity(this)
        }

        // Bottom navigation bar
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> {
                    startActivity(Intent(this, Dashboard::class.java))
                    true
                }
                R.id.settingsID -> {
                    startActivity(Intent(this, Settings::class.java))
                    true
                }
                R.id.projectID -> {
                    startActivity(Intent(this, ExistingProject::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun clearFields() {
        binding.AddProjName.text.clear()
        binding.Descriptiontxt.text.clear()
        binding.txtstartTime.text.clear()
        binding.txtEndTime.text.clear()
    }
}
