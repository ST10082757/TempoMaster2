package com.example.tempomaster

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempomaster.databinding.ActivityExistingProjectBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExistingProject : AppCompatActivity() {
    private val iintent = TheIntentHelper()
    private lateinit var recyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var projectList: MutableList<Projects>
    private lateinit var databaseReference: DatabaseReference

    private lateinit var binding: ActivityExistingProjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExistingProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Projects")

        // Initialize the project list and adapter
        projectList = mutableListOf()
        projectAdapter = ProjectAdapter(projectList)

        // Set up RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = projectAdapter

        // Fetch data from Firebase
        fetchProjectsFromFirebase()

        // Handle the camera intent
        val image: ImageView = binding.projectPngView
        val bitmap = intent.getParcelableExtra<Bitmap>("ProjectImage")
        if (bitmap != null) {
            image.setImageBitmap(bitmap)
        } else {
            image.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image
        }

        // Bottom navigation handling
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

        binding.rtnBackBtn.setOnClickListener {
            iintent.startAddProjectActivity(this, Dashboard::class.java)
        }
        // Update this part
        binding.btngoalsetting.setOnClickListener {
            val intent = Intent(this, Goals::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProjectsFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
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
    }

    class ProjectAdapter(private val projectList: List<Projects>) :
        RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

        inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val projectName: TextView = itemView.findViewById(R.id.projectName)
            val projectDescription: TextView = itemView.findViewById(R.id.projectDescription)
            val startDate: TextView = itemView.findViewById(R.id.startDate)
            val endDate: TextView = itemView.findViewById(R.id.endDate)
            val category: TextView = itemView.findViewById(R.id.category)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
            return ProjectViewHolder(view)
        }

        override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
            val project = projectList[position]
            holder.projectName.text = project.Pname
            holder.projectDescription.text = project.description
            holder.startDate.text = project.startTime
            holder.endDate.text = project.endTime
            holder.category.text = project.category
        }

        override fun getItemCount(): Int {
            return projectList.size
        }
    }
}

private fun TheIntentHelper.startAddProjectActivity(context: ExistingProject, activityToOpen: Class<*>) {
    val intent = Intent(context, activityToOpen)
    context.startActivity(intent)
}



