package com.example.tempomaster

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempomaster.databinding.ActivityExistingProjectBinding
import com.google.firebase.database.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class ExistingProject : AppCompatActivity() {
    private lateinit var binding: ActivityExistingProjectBinding
    private val intentHelper = TheIntentHelper()
    private lateinit var projectList: MutableList<Projects>
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var myAdapter: MyAdapter
    private lateinit var list: ArrayList<Projects>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExistingProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.ProjectRecycleView
        database = FirebaseDatabase.getInstance().getReference("projects")
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        list = ArrayList()
        myAdapter = MyAdapter(this, list)
        recyclerView.adapter = myAdapter

        // Load all projects without filtering by userId
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (projectSnapshot in dataSnapshot.children) {
                    val project = projectSnapshot.getValue(Projects::class.java)
                    if (project != null) {
                        list.add(project)
                    }
                }
                myAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@ExistingProject, "Failed to load projects", Toast.LENGTH_SHORT).show()
            }
        })

        val image: ImageView = binding.projectPngView
        val bitmap = intent.getParcelableExtra<Bitmap>("ProjectImage")
        if (bitmap != null) {
            image.setImageBitmap(bitmap)
        } else {
            image.setImageResource(R.drawable.ic_launcher_foreground) // Placeholder image
        }

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
                    startActivity(Intent(this, ProjectList::class.java))
                    true
                }
                else -> false
            }
        }

        binding.rtnBackBtn.setOnClickListener {
            intentHelper.startAddProjectActivity(this, Dashboard::class.java)
        }

        binding.btngoalsetting.setOnClickListener {
            intentHelper.startAddProjectActivity(this, Goals::class.java)
            val intent = Intent(this, Goals::class.java)
            startActivity(intent)
        }
    }
}

private fun TheIntentHelper.startAddProjectActivity(context: ExistingProject, activityToOpen: Class<*>) {
    val intent = Intent(context, activityToOpen)
    context.startActivity(intent)
}
