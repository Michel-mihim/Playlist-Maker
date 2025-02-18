package com.practicum.playlistmaker.player.domain.models

sealed interface PlayerActivityState {
    data class PlayerCurrentState(
        val playerActivityPlayerReadiness: Boolean,
        val playerActivityPlayerStatus: PlayerStatus,
        val playerActivityTrackProgress: String
        ) : PlayerActivityState
}