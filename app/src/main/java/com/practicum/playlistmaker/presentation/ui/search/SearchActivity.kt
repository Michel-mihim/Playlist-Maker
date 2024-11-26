package com.practicum.playlistmaker.presentation.ui.search

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
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.domain.searchTracks.models.SearchStatus
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.searchTracks.models.SearchTracksResult
import com.practicum.playlistmaker.domain.searchTracks.models.Track
import com.practicum.playlistmaker.presentation.presenter.TrackAdapter
import com.practicum.playlistmaker.domain.searchTracks.api.TracksInteractor
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.presentation.ui.player.PlayerActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SearchActivity : AppCompatActivity() {

    //инициализированные объекты====================================================================
    private val tracks = ArrayList<Track>()
    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())

    private var searchEmpty: String = Constants.SEARCH_EMPTY

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
    private lateinit var searchProgressBar: ProgressBar
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

        //инициализация объектов
        adapter = TrackAdapter(tracks)

        sharedPrefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)

        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            Log.d("WTF", "Слушатель изменения файла сработал для "+this.toString())
            if (key == Constants.SEARCH_HISTORY_KEY) showHistory(searchHistory)
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
        searchProgressBar = findViewById(R.id.search_progress_bar)

        //основной листинг==========================================================================
        searchFieldMakeEmpty()
        openSoftKeyBoard(this@SearchActivity, manager, searchEdittext)
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
            bundle.putString("b_previewUrl", track.previewUrl)
            playerIntent.putExtras(bundle)
            startActivity(playerIntent)

            writeHistory(searchHistory, track)
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
            manager.showSoftInput(searchEdittext, InputMethodManager.SHOW_IMPLICIT)
            showHistory(searchHistory)
            searchViewsHide()
            historyViewsShow()
            //imm.hideSoftInputFromWindow(currentFocus!!.windowToken,0) - так прячется клавиатура
            //search_editText.clearFocus()
        }

        searchRenewButton.setOnClickListener {
            if (searchEdittext.text.isNotEmpty()) {
                searchDebounce()
            }
        }

        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = searchClearButtonVisibility(s)
                Log.d("wtf", s.toString())
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                searchEmpty = s.toString()
            }
        }
        searchEdittext.addTextChangedListener(textWatcher)

    }

    //расчетные функции=============================================================================
    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable) // runnable - fun searchRequest()
        handler.postDelayed(searchRunnable, Constants.SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(){
        if (searchEdittext.text.isNotEmpty()) {
            historyViewsHide()
            searchViewsHide()
            hidePlaceholder()
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(sharedPrefsListener)
            showSearchProgressbar()
            tracks.clear()

            val tracksInteractor = Creator.getTracksInteractor()

            tracksInteractor.searchTracks(searchEdittext.text.toString(), object : TracksInteractor.TracksConsumer {
                override fun consume(result: SearchTracksResult) {

                    handler.post{
                        when (result) {
                            is SearchTracksResult.Success -> {
                                tracks.addAll(result.tracks)
                                showStatus(SearchStatus.TRACKS_FOUND, Constants.SEARCH_SUCCESS)
                            }
                            is SearchTracksResult.Empty -> {
                                tracks.addAll(result.tracks)
                                showStatus(SearchStatus.TRACKS_NOT_FOUND, Constants.TRACKS_NOT_FOUND_2)
                            }
                            is SearchTracksResult.Failure -> {
                                tracks.addAll(result.tracks)
                                showStatus(SearchStatus.ERROR_OCCURRED,"Код ошибки: ${result.code}")
                            }
                        }

                        hideSearchProgressbar()
                        searchRecyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        searchViewsShow()
                    }

                }
            })
        }
    }

    private fun writeHistory(searchHistory: SearchHistory, trackClicked: Track) {
        searchHistory.writeHistory(trackClicked)
        Log.d("WTF", "История записалась")
    }

    private fun clearHistory(searchHistory: SearchHistory) {
        searchHistory.clearHistory()

        Toast.makeText(this@SearchActivity, Constants.HISTORY_CLEARED, Toast.LENGTH_SHORT).show()
    }

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

    private fun showSearchProgressbar(){

        searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideSearchProgressbar(){

        searchProgressBar.visibility = View.INVISIBLE
    }

    private fun showStatus(indicator: SearchStatus, text: String) {
        when (indicator) {
            SearchStatus.TRACKS_NOT_FOUND -> {
                showPlaceholder(Constants.TRACKS_NOT_FOUND, R.drawable.not_found)
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.TRACKS_NOT_FOUND)
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
            SearchStatus.SOMETHING_WRONG -> {
                showPlaceholder(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.SOMETHING_WRONG)
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
            SearchStatus.TRACKS_FOUND -> {
                hidePlaceholder()
                searchRenewButton.visibility = renewButtonVisibility(SearchStatus.TRACKS_FOUND)

            }
            SearchStatus.ERROR_OCCURRED -> {
                showPlaceholder(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
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
        searchEdittext.setText(searchEmpty)
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
        outState.putString(Constants.SEARCH_STRING, searchEmpty)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEmpty = savedInstanceState.getString(Constants.SEARCH_STRING, Constants.SEARCH_EMPTY)
    }

}