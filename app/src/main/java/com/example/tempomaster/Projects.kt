package com.example.tempomaster

 class Projects {
    lateinit var date: String
    lateinit var Pname : String
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var description: String
    lateinit var category: String
}
data class Project(
    val projectName: String = "",
    val description: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val category: String = ""
)