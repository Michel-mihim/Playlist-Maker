package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //back_button
        val search_back_button = findViewById<ImageButton>(R.id.search_back_button)
        search_back_button.setOnClickListener{
            val search_back_intent = Intent(this, MainActivity::class.java)
            startActivity(search_back_intent)
        }

        //editText
        val search_editText = findViewById<EditText>(R.id.search_editText)
        val search_clear_button = findViewById<ImageButton>(R.id.search_clear_button)

        search_clear_button.visibility = View.GONE

        search_clear_button.setOnClickListener {
            search_editText.setText(getString(R.string.empty_string))
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search_clear_button.visibility = searchClearButtonVisibility(s)
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show()
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }

        search_editText.addTextChangedListener(textWatcher)
    }

    private fun searchClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}