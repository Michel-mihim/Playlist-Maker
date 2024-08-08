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
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

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

        val track_times_list = listOf(
            "5:01",
            "4:35",
            "4:10",
            "5:33",
            "5:03"
        )

        val track_images_list = listOf(
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )

        //переменные VIEW===========================================================================
        val search_back_button = findViewById<ImageButton>(R.id.search_back_button)
        val search_clear_button = findViewById<ImageButton>(R.id.search_clear_button)
        val search_editText = findViewById<EditText>(R.id.search_editText)

        val recyclerView = findViewById<RecyclerView>(R.id.searchResultsRecycler)

        //основной листинг==========================================================================
        search_clear_button.visibility = View.GONE
        search_editText.setText(search_def)

        recyclerView.adapter = SearchResultBoxAdapter(
            searchResultBoxes = List(50) {
                SearchResultBox(track_names_list[it % 5], artist_names_list[it % 5], track_times_list[it % 5], track_images_list[it % 5])
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