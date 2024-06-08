package com.example.tempomaster

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tempomaster.databinding.ActivityAddProjectBinding
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage

class AddProject : AppCompatActivity() {
    private lateinit var calendarView: CalendarView
    private lateinit var binding: ActivityAddProjectBinding
    private val intentHelper = TheIntentHelper()
    //for the camera
    private val requestImageCapture = 1
    //for the image
    private var capturedImage: Bitmap? = null
    private lateinit var camLauncher: ActivityResultLauncher<Intent>

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
                        
                        saveProjectToFirebase(date, projectName, description, startTime, endTime, timeLeft, capturedImage)
                        //saveProjectToFirebase(date,projectName, description, startTime, endTime, timeLeft, image = capturedImage )
                        
					//intentHelper.startExistingProjectActivity(this, ExistingProject::class.java, bundle)
                    }
                } else {
                    Toast.makeText(this, "Date is null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Add the return button click listener
        val backButton = findViewById<Button>(R.id.backclick)
        backButton.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish() // Optional: Call finish() if you want to close the current activity
        }
//------------------------------camera feature---------
        /*
   Using the button for camera. Val variable has been created
   because a button click action must occur when a user clicks
   on 'Take A Photo'.
	*/
        val camButton = findViewById<Button>(R.id.cameraBtn)
        
        
        /*
		checking if the camera
		button is pressed. If so, whatever was captured is retrieved
		in captured image from the Intent, and is casted it to a Bitmap
		 */
        val cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    
                    val data: Intent? = result.data
                    capturedImage = data?.extras?.get("data") as? Bitmap
                    
                    // Displaying the image in an ImageView on "ExistingProjects"
                   // val ProjectImage = findViewById<ImageView>(R.id.ProjectImageView)
                    //ProjectImage.setImageBitmap(capturedImage)
                    
                    //Alerting user the image was taken
                    Toast.makeText(this, "Picture taken", Toast.LENGTH_SHORT).show()
                } else {
                    
                    //Alerting user the image could not be taken
                    Toast.makeText(this, "Camera capture canceled", Toast.LENGTH_SHORT).show()
                }
                camButton.setOnClickListener {
                    val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (callCameraIntent.resolveActivity(packageManager) != null) {
                        camLauncher.launch(callCameraIntent)
                    }
                    
                }
            }
    }
//-----------------saving to firebase method----------------
    
    private fun saveProjectToFirebase(
        date: String,
        projectName: String,
        description: String,
        startTime: String,
        endTime: String,
        timeLeft: String,
        image: Bitmap?
    ) {
        
        //database and projectRef is needed to save a input into projects on firebase
        val database = FirebaseDatabase.getInstance()
        val projectsRef = database.getReference("projects")
        
        val projectId = projectsRef.push().key ?: UUID.randomUUID().toString()
        val project = Projects(date, projectName, description, startTime, endTime, timeLeft)
        
        //checking whether the image was taken or not on order to store it
        if (image != null) {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/$projectId.jpg")
            
            //conversions to enable the image to be in a code version
            val bass = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, bass)
            val data = bass.toByteArray()
            
            //actually storing the image
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    project.imageUrl = uri.toString()
                    projectsRef.child(projectId).setValue(project).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            
                            Toast.makeText(this, "Failed to save project", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
                navigateToDashboard()
            }
        } else {
            projectsRef.child(projectId).setValue(project).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Project saved successfully", Toast.LENGTH_SHORT).show()
                navigateToDashboard() //after project is saved, user is navigated back to dashboard
                } else {
                    Toast.makeText(this, "Failed to save project", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    //--------after project input, navigate back to dashboard
    private fun navigateToDashboard() {
        val intent = Intent(this, Dashboard::class.java)
        startActivity(intent)
        finish() // Close the current activity
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
        var imageUrl: String? = null // Make sure to initialize the imageUrl as null
    )
}
