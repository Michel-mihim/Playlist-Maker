package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.Track

class GetPlayerIntentUseCase(private val playerIntentGetter: PlayerIntentGetter) {
    fun execute(
        track: Track,
        onPlayerIntentReady: (Any) -> Unit
    ) {
        playerIntentGetter.getPlayerIntent(track, onPlayerIntentReady)
    }
}