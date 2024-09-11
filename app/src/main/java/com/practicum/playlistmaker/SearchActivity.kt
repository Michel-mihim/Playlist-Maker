package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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
        val sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPrefs)
        adapter = TrackAdapter(tracks)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //переменные VIEW===========================================================================
        search_back_button = findViewById(R.id.search_back_button)
        search_clear_button = findViewById(R.id.search_clear_button)
        search_editText = findViewById(R.id.search_editText)
        searchRecyclerView = findViewById(R.id.searchResultsRecycler)
        historyRecyclerView = findViewById(R.id.historyRecycler)

        trackNotFoundPlaceholderImage = findViewById(R.id.placeholder_pic_not_found)
        trackNotFoundPlaceholderText = findViewById(R.id.placeholder_text_not_found)
        searchRenewButton = findViewById(R.id.search_renew_button)
        history_clear_button = findViewById(R.id.history_clear_button)
        youFoundHistoryText = findViewById(R.id.you_found_text)

        //основной листинг==========================================================================
        search_clear_button.visibility = View.INVISIBLE
        search_editText.setText(search_def)

        searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        if (showHistory(searchHistory)) historyViewsShow()

        //слушатели нажатий=========================================================================
        adapter.onItemClickListener = { track ->
            writeHistory(searchHistory, track)
        }

        search_editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (search_editText.text.isNotEmpty()) {
                    historyViewsHide()
                    search()
                }
                true
            }
            false
        }

        history_clear_button.setOnClickListener{
            historyViewsHide()
            clearHistory(searchHistory)
        }

        search_back_button.setOnClickListener{
            val search_back_intent = Intent(this, MainActivity::class.java)
            startActivity(search_back_intent)
            finish()
        }

        search_clear_button.setOnClickListener {
            search_editText.setText(getString(R.string.empty_string))
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
            search_editText.clearFocus()
        }

        searchRenewButton.setOnClickListener {
            if (search_editText.text.isNotEmpty()) {
                search()
            }
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
    private fun writeHistory(searchHistory: SearchHistory, trackAdded: Track) {
        searchHistory.writeHistory(trackAdded)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory(searchHistory: SearchHistory) {
        searchHistory.clearHistory()

        Toast.makeText(this@SearchActivity, HISTORY_CLEARED, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory(searchHistory: SearchHistory): Boolean {

        val lastTracks = searchHistory.readHistory()
        Log.d("WTF", lastTracks.toString())
        tracks.clear()
        tracks.addAll(lastTracks)
        if (lastTracks.isEmpty()) return false
        historyRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        return true
    }

    private fun search() {
        iTunesService.search(search_editText.text.toString()).enqueue(object : Callback<SearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                tracks.clear()

                when (response.code()) {
                    200 -> {
                        if (response.body()?.results!!.isNotEmpty() == true) {
                            tracks.addAll(response.body()?.results!!)
                            showStatus(SearchStatus.TRACKS_FOUND, SEARCH_SUCCESS)
                        } else {
                            showStatus(SearchStatus.TRACKS_NOT_FOUND, TRACKS_NOT_FOUND_2)
                        }
                    } else -> {
                        showStatus(SearchStatus.ERROR_OCCURED,"Код ошибки: ${response.code()}")
                    }
                }

                searchRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                tracks.clear()
                showStatus(SearchStatus.SOMETHING_WRONG, SOMETHING_WRONG)
                searchRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showStatus(indicator: SearchStatus, text: String) {
        when (indicator) {
            SearchStatus.TRACKS_NOT_FOUND -> {
                showPlaceholder(TRACKS_NOT_FOUND, R.drawable.not_found)
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.TRACKS_NOT_FOUND)
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
            SearchStatus.SOMETHING_WRONG -> {
                showPlaceholder(NETWORK_PROBLEM, R.drawable.net_trouble)
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.SOMETHING_WRONG)
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
            SearchStatus.TRACKS_FOUND -> {
                hidePlaceholder()
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.TRACKS_FOUND)

            }
            SearchStatus.ERROR_OCCURED -> {
                showPlaceholder(NETWORK_PROBLEM, R.drawable.net_trouble)
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.ERROR_OCCURED)
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPlaceholder(text: String, image: Int) {
        trackNotFoundPlaceholderText.text = text
        trackNotFoundPlaceholderImage.setImageResource(image)
        trackNotFoundPlaceholderText.visibility = View.VISIBLE
        trackNotFoundPlaceholderImage.visibility = View.VISIBLE
    }

    private fun hidePlaceholder() {
        trackNotFoundPlaceholderText.visibility = View.INVISIBLE
        trackNotFoundPlaceholderImage.visibility = View.INVISIBLE
    }

    private fun renewButtonVisibility(indicator: SearchStatus): Int {
        return when (indicator) {
            SearchStatus.TRACKS_FOUND -> View.INVISIBLE
            SearchStatus.TRACKS_NOT_FOUND -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    private fun historyViewsHide() {
        youFoundHistoryText.visibility = View.INVISIBLE
        history_clear_button.visibility = View.INVISIBLE
        historyRecyclerView.visibility = View.INVISIBLE
    }

    private fun historyViewsShow() {
        youFoundHistoryText.visibility = View.VISIBLE
        history_clear_button.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.VISIBLE
    }

    private fun searchClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    //константы=====================================================================================
    companion object {
        const val SEARCH_STRING = "SEARCH_STRING"
        const val SEARCH_DEF = ""
        const val TRACKS_NOT_FOUND = "Ничего не нашлось"
        const val TRACKS_NOT_FOUND_2 = "Ничего не найдено"
        const val NETWORK_PROBLEM = "Проблемы со связью\n" +
                "\n" +
                "Загрузка не удалась. Проверьте подключение к интернету"
        const val SOMETHING_WRONG = "Что-то пошло не так.."
        const val SEARCH_SUCCESS = "Поиск успешно произведен!"
        const val HISTORY_CLEARED ="История поиска была удалена"
    }

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private  val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApi::class.java)

    private val tracks = ArrayList<Track>()

    private lateinit var adapter: TrackAdapter

    private lateinit var search_back_button: ImageButton
    private lateinit var search_clear_button: ImageButton
    private lateinit var search_editText: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView

    private lateinit var trackNotFoundPlaceholderImage: ImageView
    private lateinit var trackNotFoundPlaceholderText: TextView
    private lateinit var searchRenewButton: Button
    private lateinit var history_clear_button: Button
    private lateinit var youFoundHistoryText: TextView

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