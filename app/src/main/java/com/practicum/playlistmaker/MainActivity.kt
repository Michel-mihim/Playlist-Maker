package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // button_search_reaction-------------------------------------------------------------------
        val buttonSearch = findViewById<Button>(R.id.button_search)
        buttonSearch.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        //------------------------------------------------------------------------------------------

        // button_lib_reaction----------------------------------------------------------------------
        val buttonLib = findViewById<Button>(R.id.button_lib)

        buttonLib.setOnClickListener {
            val displayIntent = Intent(this, LibActivity::class.java)
            startActivity(displayIntent)
        }
        //------------------------------------------------------------------------------------------

        // button_settings_reaction-----------------------------------------------------------------
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
        //------------------------------------------------------------------------------------------

        Log.d("MainActivity", "Я выполнилось")
    }
}