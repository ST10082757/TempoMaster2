package com.example.tempomaster.com.example.tempomaster


import android.app.Activity
import android.content.Intent
import android.os.Bundle


fun openIntent(activity: Activity, project: String, activityToOpen: Class<*>){

    val activityContext = activity as? Activity ?: return

    val intent = Intent(activityContext, activityToOpen)

    intent.putExtra("project",project)

    activity.startActivity(intent)

}

fun shareIntent(activity: Activity, project: String)
{
    var sendIntent = Intent()

    sendIntent.setAction(Intent.ACTION_SEND)

    sendIntent.putExtra(Intent.EXTRA_TEXT, project)

    sendIntent.setType("text/plain")

    var shareIntent = Intent.createChooser(sendIntent, null)

    activity.startActivity(shareIntent)
}

fun shareIntent(activity: Activity, project: ProjectCategory)
{
    var sendIntent = Intent()

    sendIntent.setAction(Intent.ACTION_SEND)

    var shareProjectCategory = Bundle()
    shareProjectCategory.putString("projectName",project.projectName)
    shareProjectCategory.putString("timeSpent",project.projectTimeSpent)
    shareProjectCategory.putString("timeLeft",project.timeLeft)

    sendIntent.putExtra(Intent.EXTRA_TEXT,shareProjectCategory)

    sendIntent.setType("text/plain")

    var shareIntent = Intent.createChooser(sendIntent,null)
    activity.startActivity(shareIntent)
}