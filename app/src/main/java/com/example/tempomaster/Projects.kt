package com.example.tempomaster

class Projects {
    //private var _date: String = ""

    /*
    *this. helps to access members (methods or variables) of the current
    * object from within an instance method or a constructor.
    */
    lateinit var date: String
    lateinit var Pname : String
    lateinit var startTime: String
    lateinit var endTime: String
    lateinit var description: String
    lateinit var category: String


    // Primary constructor
    constructor()
//---------------------------------------------------------------------

    // Secondary constructor with all parameters
    constructor(date: String,Pname: String, startTime: String, endTime: String, description: String, category: String) : this() {
        this.date = date
        this.Pname = Pname
        this.startTime = startTime
        this.endTime = endTime
        this.description = description
        this.category = category
    }
/*This method is called when a date is selected in the DatePickerDialog

fun setDate(date: String)
{
    //this.date= date
    _date = date
}*/

}