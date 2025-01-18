package com.practicum.playlistmaker.search.ui

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
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.OnHistoryUpdatedListener
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.search.domain.models.SearchActivityState


class SearchActivity : ComponentActivity() {

    //инициализированные объекты====================================================================
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    private var searchDef: String = Constants.SEARCH_DEF


    //не инициализированные объекты=================================================================
    private lateinit var inputManager: InputMethodManager

    private lateinit var searchViewModel: SearchViewModel

    private val adapter = TracksAdapter (
        object : TracksAdapter.TrackClickListener {
            override fun onTrackClick(track: Track) {
                if (clickDebouncer()) {
                }
            }

            override fun onLikeClick(track: Track) {

            }
        }
    )


    //интеракторы===================================================================================

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

        searchViewModel = ViewModelProvider(this, SearchViewModel.getSearchViewModelFactory())[SearchViewModel::class.java]

        searchViewModel.observeSearchActivityState().observe(this) {
            render(it)
        }

        searchViewModel.observeSearchActivityToastState().observe(this) { message ->
            showToast(message)
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

        /*
        onHistoryUpdatedListener = OnHistoryUpdatedListener {
            if (searchStatus != SearchStatus.TRACKS_FOUND)
                downloadHistory(historyTracksInteractor)
        }
        */
        searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        searchViewModel.showHistory()

        //слушатели=================================================================================
        /*
        adapter.onItemClickListener = { track ->
            if (clickDebounce()) {
                //запуск плеера
                val playerIntent = Intent(this, PlayerActivity::class.java)

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

                //writeHistory(historyTracksInteractor, track)
                viewsVisibilityControl()
            }
        }
        */
        /*
        historyTracksInteractor.SetOnHistoryUpdatedListener(onHistoryUpdatedListener)

        historyClearButton.setOnClickListener{
            searchStatus = SearchStatus.DEFAULT
            viewsVisibilityControl()
            clearHistory(historyTracksInteractor)
        }
         */
        searchBackButton.setOnClickListener{
            finish()
        }

        searchClearButton.setOnClickListener {
            searchEdittext.setText(Constants.SEARCH_DEF)

        }

        /*
        searchRenewButton.setOnClickListener {
            if (searchEdittext.text.isNotEmpty()) {
                if (clickDebounce()) renewRequest()
            }
        }
         */

        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = searchClearButtonVisibility(s)
                searchViewModel.searchDelayed(
                    changedText = s?.toString() ?: ""
                )
                /*
                if (s.isNullOrEmpty()) {
                    if (isHistoryPresents(historyTracksInteractor)) {
                        downloadHistory(historyTracksInteractor)
                        searchStatus = SearchStatus.HISTORY_PLACEHOLDER
                    } else searchStatus = SearchStatus.DEFAULT
                    viewsVisibilityControl()
                }
                 */
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEdittext.addTextChangedListener(textWatcher)
    }

    //успокоители===================================================================================
    /*
    private fun renewRequest(){
        handler.removeCallbacks(searchRunnable)
        handler.post(searchRunnable)
    }
     */

    private fun clickDebouncer() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun writeHistory(historyTracksInteractor: HistoryTracksInteractor, trackClicked: Track) {
        historyTracksInteractor.addTrack(trackClicked)
    }

    private fun clearHistory(historyTracksInteractor: HistoryTracksInteractor) {
        historyTracksInteractor.clearTracks()
        Toast.makeText(this@SearchActivity, Constants.HISTORY_CLEARED, Toast.LENGTH_SHORT).show()
    }

    //управление видимостью=========================================================================
    private fun render(state: SearchActivityState) {
        when (state) {
            is SearchActivityState.Loading -> showLoading()
            is SearchActivityState.Default -> showDefault()
            is SearchActivityState.Empty -> showEmpty()
            is SearchActivityState.Content -> showContent(state.tracks)
            is SearchActivityState.Error -> showError()
            is SearchActivityState.History -> showHistory(state.tracks)
        }
    }

    private fun showLoading() {
        historyViewsHide()
        searchViewsHide()
        hidePlaceholder()
        showSearchProgressbar()
        searchRenewButton.visibility = View.INVISIBLE
    }

    private fun showDefault() {
        historyViewsHide()
        hidePlaceholder()
        searchClearButton.visibility = View.INVISIBLE
        openSoftKeyBoard(inputManager, searchEdittext)
        searchRenewButton.visibility = View.INVISIBLE
    }

    private fun showHistory(tracks: List<Track>) {
        hideSearchProgressbar()
        hidePlaceholder()
        searchRenewButton.visibility = View.INVISIBLE
        searchViewsHide()
        historyViewsShow()

        historyRecyclerView.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        hideSearchProgressbar()
        hidePlaceholder()
        searchRenewButton.visibility = View.INVISIBLE
        historyViewsHide()
        searchViewsShow()

        searchRecyclerView.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        hideSearchProgressbar()
        searchViewsHide()
        showPlaceholder(Constants.TRACKS_NOT_FOUND, R.drawable.not_found)
        searchRenewButton.visibility = View.INVISIBLE
    }

    private fun showError() {
        hideSearchProgressbar()
        searchViewsHide()
        showPlaceholder(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
        searchRenewButton.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /*
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

     */

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