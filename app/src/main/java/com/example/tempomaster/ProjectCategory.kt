package com.example.tempomaster.com.example.tempomaster

class ProjectCategory
{
    //declaring variables to hold project data
    var projectCategory : String = ""
    var projectName: String = ""
    var timeLeft: String = ""

    //default constructor
    constructor()
    {

    }
    //generating a constructor
    constructor(pCategory : String)
    {
        projectCategory = pCategory
    }
    //generating a second parameterised constructor
    constructor(pName: String, pTimeLeft: String) : this(pName)
    {
        projectName = pName
        timeLeft = pTimeLeft
    }
}