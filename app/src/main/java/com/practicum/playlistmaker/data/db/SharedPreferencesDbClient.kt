package com.practicum.playlistmaker.data.db

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.data.PREFERENCES
import com.practicum.playlistmaker.data.DbClient

class SharedPreferencesDbClient : DbClient {

    private lateinit var sharedPrefs: SharedPreferences


    override fun readTracks() {
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)


    }

    override fun writeTrack() {

    }
}