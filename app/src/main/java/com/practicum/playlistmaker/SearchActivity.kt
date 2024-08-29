package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class SearchActivity : AppCompatActivity() {

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private  val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)

    private lateinit var search_back_button: ImageButton
    private lateinit var search_clear_button: ImageButton
    private lateinit var search_editText: EditText
    private lateinit var searchRecyclerView: RecyclerView

    private lateinit var trackNotFoundPlaceholderImage: ImageView
    private lateinit var trackNotFoundPlaceholderText: TextView

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

        //переменные VIEW===========================================================================
        search_back_button = findViewById(R.id.search_back_button)
        search_clear_button = findViewById(R.id.search_clear_button)
        search_editText = findViewById(R.id.search_editText)
        searchRecyclerView = findViewById(R.id.searchResultsRecycler)

        trackNotFoundPlaceholderImage = findViewById(R.id.placeholder_pic_not_found)
        trackNotFoundPlaceholderText = findViewById(R.id.placeholder_text_not_found)

        //основной листинг==========================================================================
        search_clear_button.visibility = View.GONE
        search_editText.setText(search_def)

        //слушатели нажатий=========================================================================
        search_editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (search_editText.text.isNotEmpty()) {
                    search()
                }
                true
            }
            false
        }

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
    private fun search() {
        iTunesService.search(search_editText.text.toString()).enqueue(object : Callback<SearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results!!.isNotEmpty() == true) {
                            //заполняем адаптер
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)

                            searchRecyclerView.adapter = TrackAdapter(tracks)
                            searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
                            adapter.notifyDataSetChanged()
                            showStatus(2, null)
                        } else {
                            showStatus(0, null)
                        }
                    } else -> {
                        showStatus(1, response.code())
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showStatus(3, null)
            }
        })
    }

    private fun showStatus(indicator: Int, error_code: Int?) {
        when (indicator) {
            0 -> {
                trackNotFoundPlaceholderText.visibility = View.VISIBLE
                trackNotFoundPlaceholderImage.visibility = View.VISIBLE
                tracks.clear()
                adapter.notifyDataSetChanged()
                Toast.makeText(this@SearchActivity, "Ничего не найдено", Toast.LENGTH_SHORT).show()
            }
            1 -> {
                Toast.makeText(this@SearchActivity, "Код ошибки: ${error_code}", Toast.LENGTH_SHORT).show()
            }
            2 -> {
                trackNotFoundPlaceholderText.visibility = View.GONE
                trackNotFoundPlaceholderImage.visibility = View.GONE
                Toast.makeText(this@SearchActivity, "Поиск успешно произведен!", Toast.LENGTH_SHORT).show()
            }
            3 -> {
                Toast.makeText(this@SearchActivity, "Что-то пошло не так..", Toast.LENGTH_SHORT).show()
            }
        }


    }

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