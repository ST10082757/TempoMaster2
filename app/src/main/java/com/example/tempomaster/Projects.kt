package com.example.tempomaster

import android.graphics.Bitmap

// Class representing a project
 class Projects {
    lateinit var date: String
    lateinit var Pname: String
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var description: String
    lateinit var category: String
    val userId: String = ""
    lateinit var image: Bitmap
}

// Data class representing a project with default values
data class Project(
    val projectName: String = "",
    val description: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val category: String = ""
)
