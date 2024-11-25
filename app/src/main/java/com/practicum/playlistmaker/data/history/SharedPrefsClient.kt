package com.practicum.playlistmaker.data.history

interface SharedPrefsClient {
    fun writeTrack()

    fun readTracks()
}