package com.practicum.playlistmaker.data

interface DbClient {
    fun writeTrack()

    fun readTracks()
}