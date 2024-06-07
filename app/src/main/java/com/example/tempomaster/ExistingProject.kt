package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.tempomaster.databinding.ActivityExistingProjectBinding
// part 3
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class ExistingProject : AppCompatActivity() {
    private var iintent = TheIntentHelper()
    // Part 3
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var projectList: MutableList<Projects>
    private lateinit var databaseReference: DatabaseReference

    private lateinit var binding: ActivityExistingProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_existing_project)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Projects")

        binding = ActivityExistingProjectBinding.inflate(layoutInflater) // Correct binding
        setContentView(binding.root)

        // Initialize the project list and adapter for part 3
        projectList = mutableListOf()
        projectAdapter = ProjectAdapter(projectList)

        // Set up RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = projectAdapter

        // Fetch data from Firebase
        fetchProjectsFromFirebase()

            // the list of projects
            val bundle = intent.extras
            val date = bundle?.getString("Date")
            val projectName = bundle?.getString("Project Name")
            val description = bundle?.getString("Description")
            val startTime = bundle?.getString("Start Time")
            val endTime = bundle?.getString("End Time")

        //----------------------------------NAVIGATION BAR-----------------------------------------//
        // Check initialization of the bottom navigation
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardID -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                }
                R.id.settingsID -> {
                    val intent = Intent(this, Settings::class.java)
                    startActivity(intent)
                }
                R.id.projectID -> {
                    val intent = Intent(this, ExistingProject::class.java)
                    startActivity(intent)
                }
                R.id.projectID -> {
                    val intent = Intent(this, ExistingProject::class.java)
                    startActivity(intent)
                }
                else -> false // Unhandled case
            }
            true // Indicate successful handling
        }

        val  button = findViewById<Button>(R.id.rtnBackBtn)
        button.setOnClickListener{
            iintent.startAddProjectActivity(this,Dashboard::class.java)
        }
        val  setGoal = findViewById<Button>(R.id.btngoalsetting)
        setGoal.setOnClickListener{
            iintent.startAddProjectActivity(this,Goals::class.java)
        }

        // Handle the camera intent
        val image: ImageView = findViewById(R.id.projectPngView)
        val bitmap = intent.getParcelableExtra<Bitmap>("ProjectImage")
        image.setImageBitmap(bitmap)
    }
//--------------------Part 3--------------------------//
// Fetches the data from the firebase database
    private fun fetchProjectsFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                projectList.clear()
                for (projectSnapshot in dataSnapshot.children) {
                    val project = projectSnapshot.getValue(Projects::class.java)
                    if (project != null) {
                        projectList.add(project)
                    }
                }
                projectAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ExistingProject, "Failed to load projects", Toast.LENGTH_SHORT).show()
            }
        })
    }
    class ProjectAdapter(private val projectList: List<Projects>) :
        RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

        inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val projectName: TextView = itemView.findViewById(R.id.projectName)
            val projectDescription: TextView = itemView.findViewById(R.id.projectDescription)
            val projectDates: TextView = itemView.findViewById(R.id.projectDates)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_project, parent, false)
            return ProjectViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
            val currentItem = projectList[position]
            holder.projectName.text = currentItem.Pname
            holder.projectDescription.text = currentItem.description
            holder.projectDates.text = "${currentItem.date} - ${currentItem.endTime}"
        }
        override fun getItemCount() = projectList.size
    }
}