import android.content.Context
import android.content.Intent
import com.example.tempomaster.databinding.ActivityAddProjectBinding

class IntentHelper {
    fun openIntent(context: Context, aProject: aProject, activityToOpen: Class<*>) {
        val intent = Intent(context, activityToOpen)
        context.startActivity(intent)
    }



}

