package com.practicum.playlistmaker.search.data.impl

import android.content.Intent
import android.os.Bundle
import com.practicum.playlistmaker.search.domain.api.PlayerIntentGetter
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.constants.Constants
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
        bundle.putString(Constants.TRACK_NAME_KEY, track.trackName)
        bundle.putString(Constants.ARTIST_NAME_KEY, track.artistName)
        bundle.putString(Constants.TRACK_TIME_KEY, SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
        bundle.putString(Constants.PIC_URL_KEY, getCoverArtwork(track.artworkUrl100))
        bundle.putString(Constants.TRACK_ALBUM_KEY, track.collectionName)
        bundle.putString(Constants.TRACK_YEAR_KEY, isoDateToYearConvert(track.releaseDate))
        bundle.putString(Constants.TRACK_GENRE_KEY, track.primaryGenreName)
        bundle.putString(Constants.TRACK_COUNTRY_KEY, track.country)
        bundle.putString(Constants.PREVIEW_PIC_URL_KEY, track.previewUrl)
        playerIntent.putExtras(bundle)
        onPlayerIntentReady.invoke(playerIntent)
    }
}