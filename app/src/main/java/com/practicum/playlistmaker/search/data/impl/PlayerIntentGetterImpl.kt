package com.practicum.playlistmaker.search.data.impl

import android.content.Intent
import android.os.Bundle
import com.practicum.playlistmaker.search.data.PlayerIntentGetter
import com.practicum.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import com.practicum.playlistmaker.utils.converters.getCoverArtwork
import com.practicum.playlistmaker.utils.converters.isoDateToYearConvert

class PlayerIntentGetterImpl(
    private val playerIntent: Intent,
    private val bundle: Bundle
) : PlayerIntentGetter {
    override fun getPlayerIntent(
        track: Track,
        onPlayerIntentReady: ((Any) -> Unit)
    ) {
        bundle.putString("b_track_name", track.trackName)
        bundle.putString("b_artist_name", track.artistName)
        bundle.putString("b_track_time", SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
        bundle.putString("b_artworkUrl100", getCoverArtwork(track.artworkUrl100))
        bundle.putString("b_track_album", track.collectionName)
        bundle.putString("b_track_year", isoDateToYearConvert(track.releaseDate))
        bundle.putString("b_track_genre", track.primaryGenreName)
        bundle.putString("b_track_country", track.country)
        bundle.putString("b_previewUrl", track.previewUrl)
        playerIntent.putExtras(bundle)
        onPlayerIntentReady.invoke(playerIntent)
    }
}