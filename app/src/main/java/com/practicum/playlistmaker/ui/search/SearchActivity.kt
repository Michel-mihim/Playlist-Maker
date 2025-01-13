package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.searchTracks.models.SearchStatus
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.history.OnHistoryUpdatedListener
import com.practicum.playlistmaker.domain.history.api.HistoryTracksInteractor
import com.practicum.playlistmaker.domain.searchTracks.models.SearchTracksResult
import com.practicum.playlistmaker.domain.searchTracks.models.Track
import com.practicum.playlistmaker.domain.searchTracks.api.SearchTracksInteractor
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.ui.mediaPlayer.MediaPlayerActivity
import java.text.SimpleDateFormat
import java.util.Locale
import com.practicum.playlistmaker.utils.converters.isoDateToYearConvert
import com.practicum.playlistmaker.utils.converters.getCoverArtwork


class SearchActivity : AppCompatActivity() {

    //инициализированные объекты====================================================================
    private val tracks = ArrayList<Track>()
    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())
    private var searchDef: String = Constants.SEARCH_DEF
    private var isClickAllowed = true

    //не инициализированные объекты=================================================================
    private lateinit var adapter: TrackAdapter
    private lateinit var searchStatus: SearchStatus
    private lateinit var inputManager: InputMethodManager

    //интеракторы===================================================================================
    private lateinit var historyTracksInteractor: HistoryTracksInteractor
    private lateinit var searchTracksInteractor: SearchTracksInteractor
    private lateinit var onHistoryUpdatedListener: OnHistoryUpdatedListener

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
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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

        //инициализация объектов
        adapter = TrackAdapter(tracks)

        searchTracksInteractor = Creator.provideTracksInteractor()
        historyTracksInteractor = Creator.provideHistoryTracksInteractor(this)

        onHistoryUpdatedListener = OnHistoryUpdatedListener {
            if (searchStatus != SearchStatus.TRACKS_FOUND)
                downloadHistory(historyTracksInteractor)
        }

        searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        if (isHistoryPresents(historyTracksInteractor)) {
            searchStatus = SearchStatus.HISTORY_PLACEHOLDER
            downloadHistory(historyTracksInteractor)
        } else {
            searchStatus = SearchStatus.DEFAULT
        }
        viewsVisibilityControl()

        //слушатели=================================================================================
        adapter.onItemClickListener = { track ->
            if (clickDebounce()) {
                //запуск плеера
                val playerIntent = Intent(this, MediaPlayerActivity::class.java)

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

                writeHistory(historyTracksInteractor, track)
                viewsVisibilityControl()
            }
        }

        historyTracksInteractor.SetOnHistoryUpdatedListener(onHistoryUpdatedListener)

        historyClearButton.setOnClickListener{
            searchStatus = SearchStatus.DEFAULT
            viewsVisibilityControl()
            clearHistory(historyTracksInteractor)
        }

        searchBackButton.setOnClickListener{
            finish()
        }

        searchClearButton.setOnClickListener {
            searchEdittext.setText(Constants.SEARCH_DEF)
            if (isHistoryPresents(historyTracksInteractor))
            {
                downloadHistory(historyTracksInteractor)
                searchStatus = SearchStatus.HISTORY_PLACEHOLDER
            } else searchStatus = SearchStatus.DEFAULT
            viewsVisibilityControl()
        }

        searchRenewButton.setOnClickListener {
            if (searchEdittext.text.isNotEmpty()) {
                if (clickDebounce()) renewRequest()
            }
        }

        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = searchClearButtonVisibility(s)
                if (!s.isNullOrEmpty()) searchDebounce()
                if (s.isNullOrEmpty()) {
                    if (isHistoryPresents(historyTracksInteractor)) {
                        downloadHistory(historyTracksInteractor)
                        searchStatus = SearchStatus.HISTORY_PLACEHOLDER
                    } else searchStatus = SearchStatus.DEFAULT
                    viewsVisibilityControl()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEdittext.addTextChangedListener(textWatcher)
    }

    //успокоители===================================================================================
    private fun renewRequest(){
        handler.removeCallbacks(searchRunnable)
        handler.post(searchRunnable)
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable) // runnable - fun searchRequest()
        handler.postDelayed(searchRunnable, Constants.SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    //основные операции=============================================================================
    private fun searchRequest(){
        if (searchEdittext.text.isNotEmpty()) {
            searchStatus = SearchStatus.SEARCH_RESULT_WAITING
            viewsVisibilityControl()
            tracks.clear()
            searchTracksInteractor.searchTracks(searchEdittext.text.toString(), object : SearchTracksInteractor.TracksConsumer {
                override fun consume(result: SearchTracksResult) {
                    handler.post{
                        when (result) {
                            is SearchTracksResult.Success -> {
                                tracks.addAll(result.tracks)
                                searchStatus = SearchStatus.TRACKS_FOUND
                                showStatus(searchStatus, Constants.SEARCH_SUCCESS)
                            }
                            is SearchTracksResult.Empty -> {
                                tracks.addAll(result.tracks)
                                searchStatus = SearchStatus.TRACKS_NOT_FOUND
                                showStatus(searchStatus, Constants.TRACKS_NOT_FOUND_2)
                            }
                            is SearchTracksResult.Failure -> {
                                tracks.addAll(result.tracks)
                                searchStatus = SearchStatus.ERROR_OCCURRED
                                showStatus(searchStatus,"Код ошибки: ${result.code}")
                            }
                        }
                        searchRecyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                        viewsVisibilityControl()
                    }
                }
            })
        }
    }

    private fun writeHistory(historyTracksInteractor: HistoryTracksInteractor, trackClicked: Track) {
        historyTracksInteractor.addTrack(trackClicked)
    }

    private fun clearHistory(historyTracksInteractor: HistoryTracksInteractor) {
        historyTracksInteractor.clearTracks()
        Toast.makeText(this@SearchActivity, Constants.HISTORY_CLEARED, Toast.LENGTH_SHORT).show()
    }

    //управление видимостью=========================================================================
    private fun viewsVisibilityControl() {
        when (searchStatus) {
            SearchStatus.DEFAULT -> {
                historyViewsHide()
                hidePlaceholder()
                searchClearButton.visibility = View.INVISIBLE
                openSoftKeyBoard(inputManager, searchEdittext)
                searchRenewButton.visibility = View.INVISIBLE
            }
            SearchStatus.SEARCH_RESULT_WAITING -> {
                historyViewsHide()
                searchViewsHide()
                hidePlaceholder()
                showSearchProgressbar()
                searchRenewButton.visibility = View.INVISIBLE
            }
            SearchStatus.HISTORY_PLACEHOLDER -> {
                inputManager.showSoftInput(searchEdittext, InputMethodManager.SHOW_IMPLICIT)
                searchViewsHide()
                hidePlaceholder()
                searchClearButton.visibility = View.INVISIBLE
                searchRenewButton.visibility = View.INVISIBLE
                historyViewsShow()
            }
            SearchStatus.TRACKS_NOT_FOUND -> {
                hideSearchProgressbar()
                searchViewsShow()
                showPlaceholder(Constants.TRACKS_NOT_FOUND, R.drawable.not_found)
                searchRenewButton.visibility = View.INVISIBLE
            }
            SearchStatus.TRACKS_FOUND -> {
                hideSearchProgressbar()
                hidePlaceholder()
                searchRenewButton.visibility = View.INVISIBLE
                searchViewsShow()
            }
            SearchStatus.ERROR_OCCURRED -> {
                hideSearchProgressbar()
                searchViewsShow()
                showPlaceholder(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
                searchRenewButton.visibility = View.VISIBLE
            }
            SearchStatus.SOMETHING_WRONG -> {
                hideSearchProgressbar()
                searchViewsShow()
                showPlaceholder(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
                searchRenewButton.visibility = View.VISIBLE
            }
        }
    }

    private fun isHistoryPresents(historyTracksInteractor: HistoryTracksInteractor): Boolean {
        val lastTracks = historyTracksInteractor.getTracks()
        return lastTracks.isNotEmpty()
    }

    private fun downloadHistory(historyTracksInteractor: HistoryTracksInteractor) {
        val lastTracks = historyTracksInteractor.getTracks()
        tracks.clear()
        tracks.addAll(lastTracks)
        historyRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showStatus(searchStatus: SearchStatus, text: String) {
        when (searchStatus) {
            SearchStatus.DEFAULT -> {}
            SearchStatus.SEARCH_RESULT_WAITING -> {}
            SearchStatus.HISTORY_PLACEHOLDER -> {}
            SearchStatus.TRACKS_NOT_FOUND -> {
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
            SearchStatus.SOMETHING_WRONG -> {
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
            SearchStatus.TRACKS_FOUND -> {}
            SearchStatus.ERROR_OCCURRED -> {
                Toast.makeText(this@SearchActivity, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSearchProgressbar(){
        searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideSearchProgressbar(){
        searchProgressBar.visibility = View.INVISIBLE
    }

    private fun openSoftKeyBoard(inputManager: InputMethodManager, view: EditText) {
        view.requestFocus()
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun closeSoftKeyBoard(inputManager: InputMethodManager) {
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        searchEdittext.clearFocus()
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
        trackNotFoundPlaceholderText.visibility = View.INVISIBLE
        trackNotFoundPlaceholderImage.visibility = View.INVISIBLE
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

    //переопределение методов активити==============================================================
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Constants.SEARCH_STRING, searchDef)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchDef = savedInstanceState.getString(Constants.SEARCH_STRING, Constants.SEARCH_DEF)
    }

}