package com.example.tempomaster
import android.content.Context
import android.content.Intent
import android.os.Bundle

class TheIntentHelper {


    fun startActivity(context: Context, cls: Class<*>, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun openIntent(context: Context, aProject: String, activityToOpen: Class<*>) {
        val intent = Intent(context, activityToOpen)
        context.startActivity(intent)
    }

    public fun startAddProjectActivity(
        context: Context,
        Projects: String,
        activityToOpen: Class<*>,
        bundle: Bundle
    ) {
        val intent = Intent(context, Projects::class.java)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    /*
    overloaded shareIntent() method. This method accepts all
the same parameters as the
 original shareIntent() method, but adds the project parameter
 as the data that the app shares
     */
    fun shareIntent(context: Context, projects: Projects) {
        var sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        var shareProjectDetails = Bundle()
        shareProjectDetails.putString("Project  Date", projects.date)
        shareProjectDetails.putString("Project  Description", projects.description)
        shareProjectDetails.putString("Project  Start Time", projects.startTime)
        shareProjectDetails.putString("Project  End Time", projects.endTime)
        shareProjectDetails.putString("Project  Category", projects.category)

        //sharing the whole bundle
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareProjectDetails)
        sendIntent.setType("Project Details")

        var shareIntent = Intent.createChooser(sendIntent, null,)
        context.startActivity(sendIntent)
    }


    companion object {
        fun startAddProjectActivity(
            context: Context,
            addProject: Class<Projects>, java: Class<out Projects>, bundle: Bundle
        ) {
            val intent = Intent(context, Projects::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }


    fun startExistingProjectActivity(context: Context, cls: Class<*>, bundle: Bundle) {
        val intent = Intent(context, cls)
        intent.putExtras(bundle)
        context.startActivity(intent)


    }
    fun startingExistingProjectActivity (context: Context, cls:Class<*>)
    {
        // Set up a click listener for your button

        // Create an Intent to start SecondActivity
        val intent = Intent(context, cls)

        // Start SecondActivity
        context.startActivity(intent)
    }


    fun startAddProjectActivity(context: Context, cls:Class<*>)
    {
        // Set up a click listener for your button

            // Create an Intent to start SecondActivity
            val intent = Intent(context, cls)

            // Start SecondActivity
            context.startActivity(intent)
    }

}





