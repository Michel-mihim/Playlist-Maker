package com.practicum.playlistmaker.domain.mediaPlayer.api

interface MediaPlayerRepository {
    fun prepare(url: String?)
    fun act()
}