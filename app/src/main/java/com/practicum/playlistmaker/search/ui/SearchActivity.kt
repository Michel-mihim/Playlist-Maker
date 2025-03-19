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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.api.HistoryTracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    //инициализированные объекты====================================================================
    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    private var searchDef: String = Constants.SEARCH_DEF


    //не инициализированные объекты=================================================================
    private lateinit var inputManager: InputMethodManager

    private val searchViewModel by viewModel<SearchViewModel>()

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

    //не инициализированные views===================================================================
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
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
        
        searchViewModel.observeSearchActivityState().observe(this) {
            render(it)
        }

        searchViewModel.observeSearchActivityToastState().observe(this) { message ->
            showToast(message)
        }

        searchViewModel.observePlayerActivityIntent().observe(this) { intent ->
            Log.d("wtf", "intent player got(searchActivity_observer)")
            startActivity(intent)
        }

        //инициализация views
        searchBackButton = findViewById(R.id.search_back_button)
        searchClearButton = findViewById(R.id.search_clear_button)
        searchEdittext = findViewById(R.id.search_edit_text)
        searchRecyclerView = findViewById(R.id.search_results_recycler)
        historyRecyclerView = findViewById(R.id.history_recycler)
        placeholderImage = findViewById(R.id.placeholder_pic)
        placeholderText = findViewById(R.id.placeholder_text)
        searchRenewButton = findViewById(R.id.search_renew_button)
        historyClearButton = findViewById(R.id.history_clear_button)
        youFoundHistoryText = findViewById(R.id.you_found_text)
        searchProgressBar = findViewById(R.id.search_progress_bar)

        searchRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        historyRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)

        inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        searchViewModel.showHistory()

        //слушатели=================================================================================

        adapter.onItemClickListener = { track ->
            if (clickDebouncer()) {
                //запуск плеера
                searchViewModel.getPlayerIntent(track)
                Log.d("wtf", "intent asked(track_clicked)")

                searchViewModel.writeHistory(track)
            }
        }


        historyClearButton.setOnClickListener{
            searchViewModel.clearHistory()
        }

        searchBackButton.setOnClickListener{
            finish()
        }

        searchClearButton.setOnClickListener {
            searchEdittext.setText(Constants.SEARCH_DEF)

        }

        searchRenewButton.setOnClickListener {
            if (clickDebouncer()) renewRequest()
        }


        //переопределение функций слушателя текста==================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchClearButton.visibility = searchClearButtonVisibility(s)
                searchViewModel.searchDelayed(
                    changedText = s?.toString() ?: ""
                )

                if (s.isNullOrEmpty()) {
                    searchViewModel.showHistory()
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEdittext.addTextChangedListener(textWatcher)
    }

    private fun renewRequest(){
        searchViewModel.searchForce(searchEdittext.text.toString())
    }

    private fun clickDebouncer() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
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
        defaultViewsShow()
        showSearchProgressbar()
    }

    private fun showDefault() {
        defaultViewsShow()
    }

    private fun showHistory(tracks: List<Track>) {
        historyViewsShow()

        historyRecyclerView.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        contentViewsShow()

        searchRecyclerView.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        emptyViewsShow(Constants.TRACKS_NOT_FOUND, R.drawable.not_found)
    }

    private fun showError() {
        errorViewsShow(Constants.NETWORK_PROBLEM, R.drawable.net_trouble)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSearchProgressbar(){
        searchProgressBar.visibility = View.VISIBLE
    }

    private fun hideSearchProgressbar(){
        searchProgressBar.visibility = View.INVISIBLE
    }

    private fun defaultViewsShow() {
        hideSearchProgressbar()
        searchRecyclerView.visibility = View.INVISIBLE
        historyRecyclerView.visibility = View.INVISIBLE
        searchRenewButton.visibility = View.INVISIBLE
        historyClearButton.visibility = View.INVISIBLE
        hidePlaceholder()
        youFoundHistoryText.visibility = View.INVISIBLE
    }

    private fun historyViewsShow() {
        hideSearchProgressbar()
        searchRecyclerView.visibility = View.INVISIBLE
        historyRecyclerView.visibility = View.VISIBLE
        searchRenewButton.visibility = View.INVISIBLE
        historyClearButton.visibility = View.VISIBLE
        hidePlaceholder()
        youFoundHistoryText.visibility = View.VISIBLE
    }

    private fun emptyViewsShow(text: String, image: Int) {
        hideSearchProgressbar()
        searchRecyclerView.visibility = View.INVISIBLE
        historyRecyclerView.visibility = View.INVISIBLE
        searchRenewButton.visibility = View.INVISIBLE
        historyClearButton.visibility = View.INVISIBLE
        showPlaceholder(text, image)
        youFoundHistoryText.visibility = View.INVISIBLE
    }

    private fun contentViewsShow() {
        hideSearchProgressbar()
        searchRecyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.INVISIBLE
        searchRenewButton.visibility = View.INVISIBLE
        historyClearButton.visibility = View.INVISIBLE
        hidePlaceholder()
        youFoundHistoryText.visibility = View.INVISIBLE
    }

    private fun errorViewsShow(text: String, image: Int) {
        hideSearchProgressbar()
        searchRecyclerView.visibility = View.INVISIBLE
        historyRecyclerView.visibility = View.INVISIBLE
        searchRenewButton.visibility = View.VISIBLE
        historyClearButton.visibility = View.INVISIBLE
        showPlaceholder(text, image)
        youFoundHistoryText.visibility = View.INVISIBLE
    }

    private fun showPlaceholder(text: String, image: Int) {
        placeholderText.text = text
        placeholderImage.setImageResource(image)
        placeholderText.visibility = View.VISIBLE
        placeholderImage.visibility = View.VISIBLE
    }

    private fun hidePlaceholder() {
        placeholderText.visibility = View.INVISIBLE
        placeholderImage.visibility = View.INVISIBLE
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