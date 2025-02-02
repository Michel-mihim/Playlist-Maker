package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

interface PlayerIntentGetter {
    fun getPlayerIntent(
        track: Track,
        onPlayerIntentReady: (Any) -> Unit
    )
}