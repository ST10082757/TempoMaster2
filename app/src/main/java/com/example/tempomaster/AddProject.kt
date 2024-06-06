package com.example.tempomaster
import com.example.tempomaster.Goals
import android.view.View
import android.view.View.OnClickListener
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tempomaster.databinding.ActivityAddProjectBinding


class AddProject : AppCompatActivity() {
    private val requestImageCapture = 1

    private lateinit var resultLauncher: ActivityResultLauncher<Intent> //for photo

    //Create a new Projects object
    val projects = Projects()
    // declaring biding
    lateinit var binding: ActivityAddProjectBinding

    private lateinit var calendarView: CalendarView

    var intentHelper = TheIntentHelper()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_project)

        // initializing binsding
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get a reference to the back button
        val backButton = findViewById<Button>(R.id.backclick)

        // Set an OnClickListener for the back button
        backButton.setOnClickListener {
            // Create an Intent to navigate back to the Dashboard activity
            val intent = Intent(this, Dashboard::class.java)
            // Start the Dashboard activity
            startActivity(intent)
            // Finish the current activity to remove it from the back stack
            finish()
        }
        //calender view
        calendarView = findViewById(R.id.projectCalendar)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val DateSelected = "$dayOfMonth-${month + 1}-$year"
            projects.date = DateSelected // Set the date in  Projects object

            val projectNameInput = findViewById<EditText>(R.id.AddProjName)
            val descriptionInput = findViewById<EditText>(R.id.Descriptiontxt)
            val startTimeInput = findViewById<EditText>(R.id.txtstartTime)
            val endTimeInput = findViewById<EditText>(R.id.txtEndTime)
            val category = findViewById<EditText>(R.id.txtCategory)

            //---------------------------NAVIGATION BAR---------------------------------//
            // Check initialization of the bottom navigation
            binding.bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.dashboardID -> {  val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent) }
                    R.id.settingsID -> {
                        val intent = Intent(this, Settings::class.java)
                        startActivity(intent)
                    }
                    R.id.projectID -> {
                        val intent = Intent(this, ExistingProject::class.java)
                        startActivity(intent)
                    }
                    else -> false
                }
                true // Indicate successful handling
            }
//----------------------------------------------------------------------------------------------
            /*
        /after the button click(below), we are getting and setting
        the input values to their fields accoridng to the
        Projects object. The bundle will help display the into
        into the Existing projects activity
         */
            // Get reference to your button
            val clickToAddProj = findViewById<Button>(R.id.clickAddPrj)

            // Set up a click listener for your button
            clickToAddProj.setOnClickListener {
                // Get the input field values
                val date = DateSelected
//according to copilot, this is a way to use the string directly without the '.text'
                val projectName = projectNameInput.text.toString()
                val description = descriptionInput.text.toString()
                val startTime = startTimeInput.text.toString()
                val endTime = endTimeInput.text.toString()

// Create a Bundle to hold the data
                val bundle = Bundle()
                bundle.putString("Date", date)
                bundle.putString("Project Name", projectName)
                bundle.putString("Description", description)
                bundle.putString("Start Time", startTime)
                bundle.putString("End Time", endTime)
                //using the intent helper to send data to Existing project activity

                intentHelper.startExistingProjectActivity(this, ExistingProject::class.java, bundle)

                }
            }

//taking a photo using camera intent

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                val intent = Intent(this, ExistingProject::class.java).apply {
                    putExtra("ProjectImage", imageBitmap)
                }
                startActivity(intent)
            }
        }

        //camera button click for photo capture
        val camButton = findViewById<Button>(R.id.cameraBtn)
        camButton.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }
}