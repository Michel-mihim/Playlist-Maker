package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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
        val button_search = findViewById<Button>(R.id.button_search)

        val button_searchClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(view: View) {
                Toast.makeText(this@MainActivity, "Открываем поиск!", Toast.LENGTH_SHORT).show()
            }
        }

        button_search.setOnClickListener(button_searchClickListener)
        //------------------------------------------------------------------------------------------

        // button_lib_reaction----------------------------------------------------------------------
        val button_lib = findViewById<Button>(R.id.button_lib)

        button_lib.setOnClickListener {
            Toast.makeText(this@MainActivity, "Открываем медиатеку!", Toast.LENGTH_SHORT).show()
        }
        //------------------------------------------------------------------------------------------

        // button_settings_reaction-----------------------------------------------------------------
        val button_settings = findViewById<Button>(R.id.button_settings)

        button_settings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
        //------------------------------------------------------------------------------------------
    }
}