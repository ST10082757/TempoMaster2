package com.example.tempomaster

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.databinding.ActivityAddProjectBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.Manifest

class AddProject : AppCompatActivity() {
    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var databaseReference: DatabaseReference
    private val intentHelper = TheIntentHelper()
    
    private var dateSelected: String = ""
    private var capturedImage: Bitmap? = null
    private lateinit var camLauncher: ActivityResultLauncher<Intent>
    
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
        binding.txtstartTime.setOnClickListener {
            showTimePicker { time ->
                binding.txtstartTime.setText(
                    time
                )
            }
        }
        binding.txtEndTime.setOnClickListener {
            showTimePicker { time ->
                binding.txtEndTime.setText(
                    time
                )
            }
        }
        
        // Add project button
        binding.clickAddPrj.setOnClickListener {
            val projectName = binding.AddProjName.text.toString()
            val description = binding.Descriptiontxt.text.toString()
            val startTime = binding.txtstartTime.text.toString()
            val endTime = binding.txtEndTime.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            
            val bundle = Bundle()
            bundle.putString("Project name", projectName)
            bundle.putString("Description", description)
            bundle.putString("Start time", startTime)
            bundle.putString("End time", endTime)
            bundle.putString("Category", category)
            
            
            if (projectName.isNotEmpty() && description.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && category.isNotEmpty() && dateSelected.isNotEmpty()) {
                val project =
                    Project(projectName, description, dateSelected, startTime, endTime, category)
                databaseReference.push().setValue(project)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@AddProject,
                            "Project added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        intentHelper.startExistingProjectActivity(
                            this,
                            Dashboard::class.java,
                            bundle
                        )
                        clearFields()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@AddProject, "Failed to add project", Toast.LENGTH_SHORT)
                            .show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Back button
        binding.backclick.setOnClickListener {
            intentHelper.goBack(this)
        }

//------------------------------------------------------------------------------------------------
        /* Camera button
        camLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                capturedImage = data?.extras?.get("data") as? Bitmap
                Toast.makeText(this, "Picture taken", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera capture canceled", Toast.LENGTH_SHORT).show()
            }
        }*/
        
        /* binding.cameraBtn.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                camLauncher.launch(callCameraIntent)
            }*/
        //---------------------------------------------------------------------------------------------
        
        binding.cameraBtn.setOnClickListener {
            // Check if camera permission is granted
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request camera permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                // Launch the camera
                launchCamera()
            }
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
    
    //-----------------------CAMERA FEATURE---------------------------------------------------
    // Launch the camera to capture an image
    private fun launchCamera() {
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (callCameraIntent.resolveActivity(packageManager) != null) {
            camLauncher.launch(callCameraIntent)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }
    
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }
    
    // Handle the result of the permission requestThe code checks if the camera permission is granted,
    // requests it if necessary, and handles the permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                launchCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    //-----------------------END OF CAMERA FEATURE---------------------------------------------------
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
    
    private fun saveProjectToFirebase(
        date: String,
        projectName: String,
        description: String,
        startTime: String,
        endTime: String,
        timeLeft: String,
        image: Bitmap?
    ) {
        val database = FirebaseDatabase.getInstance()
        val projectsRef = database.getReference("projects")
        
        val projectId = projectsRef.push().key ?: UUID.randomUUID().toString()
        val project = Projects(date, projectName, description, startTime, endTime, timeLeft)
        
        if (image != null) {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/$projectId.jpg")
            
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    project.imageUrl = uri.toString()
                    projectsRef.child(projectId).setValue(project).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT)
                                .show()
                            //navigateToDashboard()
                        } else {
                            Toast.makeText(this, "Failed to save project", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        } else {
            projectsRef.child(projectId).setValue(project).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()
                    navigateToDashboard()
                } else {
                    Toast.makeText(this, "Failed to save project", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun navigateToDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
        finish()
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


    data class Projects(
        val date: String,
        val projectName: String,
        val description: String,
        val startTime: String,
        val endTime: String,
        val timeLeft: String,
        var imageUrl: String? = null
    )
}
