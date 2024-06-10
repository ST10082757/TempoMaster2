package com.example.tempomaster


class Projects {
    val date: String? = null
    val projectName: String? = null
    val description: String? = null
    val startTime: String? = null
    val endTime: String? = null
    val category: String? = null
    val imageUrl: String? = null
}

// Data class representing a project with default values
data class Project(
    val date: String = "",
    val projectName: String = "",
    val description: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val category: String = "",
    val userId: String = "",
    val imageUrl: String = ""
)
