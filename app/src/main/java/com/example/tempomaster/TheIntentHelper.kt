package com.example.tempomaster

import android.content.Context
import android.content.Intent
import android.os.Bundle

class TheIntentHelper {

    fun startAddProjectActivity(context: Context, activityToOpen: Class<*>, bundle: Bundle) {
        val intent = Intent(context, activityToOpen)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun startExistingProjectActivity(context: Context, activityToOpen: Class<*>, bundle: Bundle) {
        val intent = Intent(context, activityToOpen)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun startDashboardActivity(context: Context, activityToOpen: Class<*>, bundle: Bundle) {
        val intent = Intent(context, activityToOpen)
        intent.putExtras(bundle)
        context.startActivity(intent)
    }

    fun startExistingProjectActivity(context: Context, activityToOpen: Class<*>) {
        val intent = Intent(context, activityToOpen)
        context.startActivity(intent)
    }

    /*fun shareProjectDetails(context: Context, projects: Projects) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        val shareProjectDetails = Bundle()
        shareProjectDetails.putString("Project Date", projects.date)
        shareProjectDetails.putString("Project Description", projects.description)
        shareProjectDetails.putString("Project Start Time", projects.startTime)
        shareProjectDetails.putString("Project End Time", projects.endTime)
        shareProjectDetails.putString("Project Category", projects.category)

        // Sharing the whole bundle
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareProjectDetails)
        sendIntent.type = "text/plain"

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }*/

    fun startAddProjectActivity(context: ExistingProject, activityToOpen: Class<Dashboard>) {

    }

    fun goBack(addProject: AddProject) {

    }

    fun startCameraActivity(addProject: AddProject) {

    }
}
