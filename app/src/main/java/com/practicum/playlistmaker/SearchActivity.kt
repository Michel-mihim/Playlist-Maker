package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import okhttp3.internal.http2.Http2Connection.Listener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class SearchActivity : AppCompatActivity() {

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

    //инициализированные объекты====================================================================
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(iTunesApi::class.java)
    private val tracks = ArrayList<Track>()

    private var searchDef: String = SEARCH_DEF

    //не инициализированные объекты=================================================================
    private lateinit var adapter: TrackAdapter
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var sharedPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener
    //не инициализированные views===================================================================
    private lateinit var trackNotFoundPlaceholderImage: ImageView
    private lateinit var trackNotFoundPlaceholderText: TextView
    private lateinit var searchRenewButton: Button
    private lateinit var historyClearButton: Button
    private lateinit var youFoundHistoryText: TextView
    private lateinit var searchBackButton: ImageButton
    private lateinit var searchClearButton: ImageButton
    private lateinit var searchEdittext: EditText
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    //==============================================================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("WTF", "Новая активити создана")
        //инициализация объектов
        adapter = TrackAdapter(tracks)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            Log.d("WTF", "Слушатель изменения файла сработал для "+this.toString())
            if (key == SEARCH_HISTORY_KEY) showHistory(searchHistory)
        }

        //инициализация views
        searchBackButton = findViewById(R.id.search_back_button)
        searchClearButton = findViewById(R.id.search_clear_button)
        searchEdittext = findViewById(R.id.search_edit_text)
        searchRecyclerView = findViewById(R.id.search_results_recycler)
        historyRecyclerView = findViewById(R.id.history_recycler)
        trackNotFoundPlaceholderImage = findViewById(R.id.placeholder_pic_not_found)
        trackNotFoundPlaceholderText = findViewById(R.id.placeholder_text_not_found)
        searchRenewButton = findViewById(R.id.search_renew_button)
        historyClearButton = findViewById(R.id.history_clear_button)
        youFoundHistoryText = findViewById(R.id.you_found_text)

        //основной листинг==========================================================================
        searchFieldMakeEmpty()
        openSoftKeyBoard(this@SearchActivity, imm, searchEdittext)
        searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        if (showHistory(searchHistory)) historyViewsShow()

        //слушатели=================================================================================
        adapter.onItemClickListener = { track ->
            Log.d("WTF", "Слушатель нажатия in activity: "+track.trackName)
            //запуск плеера
            val playerIntent = Intent(this, PlayerActivity::class.java)
            Log.d("WTF", track.toString())

            val bundle = Bundle()
            bundle.putString("b_track_name", track.trackName)
            bundle.putString("b_artist_name", track.artistName)
            bundle.putString("b_track_time", SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
            bundle.putString("b_artworkUrl100", getCoverArtwork(track.artworkUrl100))
            bundle.putString("b_track_album", track.collectionName)
            bundle.putString("b_track_year", isoDateToYearConvert(track.releaseDate))
            bundle.putString("b_track_genre", track.primaryGenreName)
            bundle.putString("b_track_country", track.country)
            playerIntent.putExtras(bundle)
            startActivity(playerIntent)

            writeHistory(searchHistory, track)
        }

        searchEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchEdittext.text.isNotEmpty()) {
                    historyViewsHide()
                    searchViewsHide()
                    sharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener)
                    search()
                }
                true
            }
            false
        }

        historyClearButton.setOnClickListener{
            historyViewsHide()
            clearHistory(searchHistory)
        }

        searchBackButton.setOnClickListener{
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener)
            finish()
        }

        searchClearButton.setOnClickListener {
            searchEdittext.setText(getString(R.string.empty_string))
            imm.showSoftInput(searchEdittext, InputMethodManager.SHOW_IMPLICIT)
            showHistory(searchHistory)
            searchViewsHide()
            historyViewsShow()
            //imm.hideSoftInputFromWindow(currentFocus!!.windowToken,0) - так прячется клавиатура
            //search_editText.clearFocus()
        }

        searchRenewButton.setOnClickListener {
            if (searchEdittext.text.isNotEmpty()) {
                search()
            }
        }

        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = searchClearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchDef = s.toString()
            }
        }
        searchEdittext.addTextChangedListener(textWatcher)

    }

    //расчетные функции=============================================================================
    private fun writeHistory(searchHistory: SearchHistory, trackClicked: Track) {
        searchHistory.writeHistory(trackClicked)
        Log.d("WTF", "История записалась")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory(searchHistory: SearchHistory) {
        searchHistory.clearHistory()

        Toast.makeText(this@SearchActivity, HISTORY_CLEARED, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showHistory(searchHistory: SearchHistory): Boolean {
        val lastTracks = searchHistory.readHistory()
        if (lastTracks.isEmpty()) return false else {
            tracks.clear()
            tracks.addAll(lastTracks)
            historyRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
            Log.d("WTF", "История не пустая. Загрузилась")
        }
        return true
    }

    private fun search() {
        iTunesService.search(searchEdittext.text.toString()).enqueue(object : Callback<SearchResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                tracks.clear()

                when (response.code()) {
                    200 -> {
                        if (response.body()?.results!!.isNotEmpty()) {
                            tracks.addAll(response.body()?.results!!)
                            searchRecyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                            searchViewsShow()
                            showStatus(SearchStatus.TRACKS_FOUND, SEARCH_SUCCESS)
                        } else {
                            showStatus(SearchStatus.TRACKS_NOT_FOUND, TRACKS_NOT_FOUND_2)
                        }
                    } else -> {
                        showStatus(SearchStatus.ERROR_OCCURRED,"Код ошибки: ${response.code()}")
                    }
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                tracks.clear()
                showStatus(SearchStatus.SOMETHING_WRONG, SOMETHING_WRONG)
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
            SearchStatus.ERROR_OCCURRED -> {
                showPlaceholder(NETWORK_PROBLEM, R.drawable.net_trouble)
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.ERROR_OCCURRED)
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    //технические функции===========================================================================
    private fun getCoverArtwork(artworkUrl100: String): String {
        return artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    }

    private fun isoDateToYearConvert(isoDate: String): String {
        var year: String
        val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val targetDateFormat = SimpleDateFormat("yyyy")
        try {
            val date: Date = isoDateFormat.parse(isoDate)
            year = targetDateFormat.format(date)
        } catch (e: Exception) {
            year = ""
        }
        return year
    }

    private fun searchFieldMakeEmpty() {
        searchClearButton.visibility = View.INVISIBLE
        searchEdittext.setText(searchDef)
    }

    private fun openSoftKeyBoard(context: Context, imm: InputMethodManager, view: EditText) {
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
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

    private fun searchViewsHide() {
        searchRecyclerView.visibility = View.INVISIBLE
    }

    private fun searchViewsShow() {
        searchRecyclerView.visibility = View.VISIBLE
    }

    private fun historyViewsHide() {
        youFoundHistoryText.visibility = View.INVISIBLE
        historyClearButton.visibility = View.INVISIBLE
        historyRecyclerView.visibility = View.INVISIBLE
    }

    private fun historyViewsShow() {
        youFoundHistoryText.visibility = View.VISIBLE
        historyClearButton.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.VISIBLE
    }

    private fun searchClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    //переопределение функций памяти состояния======================================================
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_STRING, searchDef)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchDef = savedInstanceState.getString(SEARCH_STRING, SEARCH_DEF)
    }

}