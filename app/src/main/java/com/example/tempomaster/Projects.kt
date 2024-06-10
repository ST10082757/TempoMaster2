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
    lateinit var userId: String
    lateinit var image: Bitmap

    // If you need custom behavior for getters and setters, you can use property custom accessors
    // For example:
    // var date: String
    //     get() = field
    //     set(value) {
    //         field = value
    //     }
}

// Data class representing a project with default values
data class Project(
    val projectName: String = "",
    val description: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val category: String = "",
    val userId: String = "",
    val imageUrl: String = ""
)
