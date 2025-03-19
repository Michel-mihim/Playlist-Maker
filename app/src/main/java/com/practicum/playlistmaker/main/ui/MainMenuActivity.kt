package com.practicum.playlistmaker.main.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.R

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // button_search_reaction-------------------------------------------------------------------
        val buttonSearch = findViewById<Button>(R.id.button_search)
        buttonSearch.setOnClickListener{
            //val searchIntent = Intent(this, SearchActivity::class.java)
            //startActivity(searchIntent)
        }

        // button_lib_reaction----------------------------------------------------------------------
        val buttonLib = findViewById<Button>(R.id.button_lib)

        buttonLib.setOnClickListener {
            //val libIntent = Intent(this, LibActivity::class.java)
            //startActivity(libIntent)
        }

        // button_settings_reaction-----------------------------------------------------------------
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSettings.setOnClickListener {
            //val settingsIntent = Intent(this, SettingsActivity::class.java)
            //startActivity(settingsIntent)
        }
    }
}