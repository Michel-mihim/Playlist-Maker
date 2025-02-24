package com.practicum.playlistmaker.player.domain.models

data class PlayerActivityState(
    val playerActivityPlayerReadiness: Boolean,
    val playerActivityPlayerStatus: PlayerStatus,
    val playerActivityTrackProgress: String
)