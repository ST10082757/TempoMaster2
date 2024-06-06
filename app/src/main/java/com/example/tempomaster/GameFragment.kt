package com.example.tempomaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_game)

        // Get a reference to the back button
        val backButton = findViewById<Button>(R.id.backclick)

        // Set an OnClickListener for the back button
        backButton.setOnClickListener {
            // Create an Intent to navigate back to the Dashboard activity
            val intent = Intent(this, Dashboard::class.java)
            // Start the Dashboard activity
            startActivity(intent)
            // Finish the current activity to remove it from the back stack
            finish()
        }
    }
}
