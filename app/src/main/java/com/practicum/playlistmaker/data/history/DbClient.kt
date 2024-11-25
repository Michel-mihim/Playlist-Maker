package com.practicum.playlistmaker.data.history

interface DbClient {
    fun writeTrack()

    fun readTracks()
}