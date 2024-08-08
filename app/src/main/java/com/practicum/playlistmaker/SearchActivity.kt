package com.practicum.playlistmaker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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

        //переменные и списки
        val track_names_list = listOf(
            "Smells Like Teen Spirit",
            "Billie Jean",
            "Stayin' Alive",
            "Whole Lotta Love",
            "Sweet Child O'Mine"
        )

        val artist_names_list = listOf(
            "Nirvana",
            "Michael Jackson",
            "Bee Gees",
            "Led Zeppelin",
            "Guns N' Roses"
        )

        //переменные VIEW===========================================================================
        val search_back_button = findViewById<ImageButton>(R.id.search_back_button)
        val search_clear_button = findViewById<ImageButton>(R.id.search_clear_button)
        val search_editText = findViewById<EditText>(R.id.search_editText)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //основной листинг==========================================================================
        search_clear_button.visibility = View.GONE
        search_editText.setText(search_def)

        val recyclerView = findViewById<RecyclerView>(R.id.searchResultsRecycler)
        recyclerView.adapter = SearchResultBoxAdapter(
            searchResultBoxes = List(50) {
                SearchResultBox(track_names_list[it % 5], artist_names_list[it % 5])
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //слушатели нажатий=========================================================================
        search_back_button.setOnClickListener{
            val search_back_intent = Intent(this, MainActivity::class.java)
            startActivity(search_back_intent)
        }

        search_clear_button.setOnClickListener {
            search_editText.setText(getString(R.string.empty_string))
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
            search_editText.clearFocus()
        }

        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search_clear_button.visibility = searchClearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                search_def = s.toString()
            }
        }
        search_editText.addTextChangedListener(textWatcher)

    }

    //вспомогательные функции=======================================================================
    private fun searchClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //константы=====================================================================================
    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_DEF = ""
    }

    //переменная строки поиска======================================================================
    private var search_def : String = SEARCH_DEF

    //переопределение функций памяти состояния======================================================
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, search_def)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        search_def = savedInstanceState.getString(SEARCH_STRING, SEARCH_DEF)
    }

}