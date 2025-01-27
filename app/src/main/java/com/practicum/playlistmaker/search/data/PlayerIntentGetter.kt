package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.models.Track

interface PlayerIntentGetter {
    fun getPlayerIntent(
        track: Track,
        onPlayerIntentReady: (Any) -> Unit
    )
}