/*
aProject is an object of a project with the date,
the start & end time, description and category. This
objects helps us to save each project entered by
a user and is saved into the database project.
----------------------------------------------------
This class contains the
properties and constructors that
reflect the structure our data will stored in on the

 */
//--------------------------------------------------
class aProject {
/*
*this. helps to aaccess members (methods or variables) of the current
* object from within an instance method or a constructor.
 */
        lateinit var date: String
        lateinit var startTime: String
        lateinit var endTime: String
        lateinit var description: String
        lateinit var category: String

        // Primary constructor
        constructor()
//---------------------------------------------------------------------

        // Secondary constructor with all parameters
        constructor(date: String, startTime: String, endTime: String, description: String, category: String) : this() {
            this.date = date
            this.startTime = startTime
            this.endTime = endTime
            this.description = description
            this.category = category
        }

}