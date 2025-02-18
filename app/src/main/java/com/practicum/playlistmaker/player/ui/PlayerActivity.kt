package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.player.domain.models.PlayerStatus
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.models.PlayerActivityState
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.utils.converters.dimensionsFloatToIntConvert
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    //VIEWS
    private lateinit var playerBackButton: ImageButton
    private lateinit var playerTrackName: TextView
    private lateinit var playerArtistName: TextView
    private lateinit var playerTrackTime: TextView
    private lateinit var playerTrackAlbum: TextView
    private lateinit var playerTrackYear: TextView
    private lateinit var playerTrackGenre: TextView
    private lateinit var playerTrackCountry: TextView
    private lateinit var playerTrackImage: ImageView
    private lateinit var trackPlayButton: ImageButton
    private lateinit var trackProgress: TextView

    //VAL BY
    private val playerViewModel by viewModel<PlayerViewModel>()

    //основной листинг==============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerViewModel.observePlayerActivityCurrentState().observe(this) {
            renderPlayerState(it)
        }

        //инициализация views
        playerTrackName = findViewById(R.id.track_player_name)
        playerArtistName = findViewById(R.id.track_artist_name)
        playerTrackTime = findViewById(R.id.attr1_2_time)
        playerTrackAlbum = findViewById(R.id.attr2_2_album)
        playerTrackYear = findViewById(R.id.attr3_2_year)
        playerTrackGenre = findViewById(R.id.attr4_2_genre)
        playerTrackCountry = findViewById(R.id.attr5_2_country)
        playerTrackImage = findViewById(R.id.player_track_image)
        playerBackButton = findViewById(R.id.player_back_button)
        trackPlayButton = findViewById(R.id.button_play_2)
        trackPlayButton.isEnabled = false //пока плеер не готов на нее нельзя нажимать
        trackProgress = findViewById(R.id.track_player_progress)

        //основной листинг
        val bundle = intent.extras
        if (bundle != null) {
            playerTrackName.text = bundle.getString(Constants.TRACK_NAME_KEY)
            playerArtistName.text = bundle.getString(Constants.ARTIST_NAME_KEY)
            playerTrackTime.text = bundle.getString(Constants.TRACK_TIME_KEY)
            playerTrackAlbum.text = bundle.getString(Constants.TRACK_ALBUM_KEY)
            playerTrackYear.text = bundle.getString(Constants.TRACK_YEAR_KEY)
            playerTrackGenre.text = bundle.getString(Constants.TRACK_GENRE_KEY)
            playerTrackCountry.text = bundle.getString(Constants.TRACK_COUNTRY_KEY)

            val cornerDp = resources.getDimension(R.dimen.track_poster_corner)
            val cornerPx = dimensionsFloatToIntConvert(cornerDp, this)
            Glide.with(this)
                .load(bundle.getString(Constants.PIC_URL_KEY))
                .placeholder(R.drawable.placeholder_large)
                .transform(RoundedCorners(cornerPx))
                .into(playerTrackImage)
        }

        playerViewModel.mediaPlayerPrepare(bundle?.getString((Constants.PREVIEW_PIC_URL_KEY)))

        //слушатели нажатий
        playerBackButton.setOnClickListener{
            finish()
        }

        trackPlayButton.setOnClickListener {
            if (clickDebouncer()) {
                playerViewModel.playbackControl()
            }

        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    private fun renderPlayerState(state: PlayerActivityState) {
        trackPlayButtonActivate(state.playerActivityPlayerReadiness)
        renderTrackPlayButton(state.playerActivityPlayerStatus)
        setTrackProgress(state.playerActivityTrackProgress)
    }

    private fun trackPlayButtonActivate(ready: Boolean) {
        trackPlayButton.isEnabled = ready
    }

    private fun renderTrackPlayButton(status: PlayerStatus) {
        when (status) {
            PlayerStatus.STATE_DEFAULT -> {}
            PlayerStatus.STATE_PREPARED -> setTrackPlayButtonPlay()
            PlayerStatus.STATE_PAUSED -> setTrackPlayButtonPlay()
            PlayerStatus.STATE_PLAYING -> setTrackPlayButtonPause()
        }
    }

    private fun setTrackPlayButtonPlay() {
        trackPlayButton.setImageResource(R.drawable.track_play)
    }

    private fun setTrackPlayButtonPause() {
        trackPlayButton.setImageResource(R.drawable.track_pause)
    }

    private fun setTrackProgress(progress: String) {
        trackProgress.text = progress
    }

    private fun clickDebouncer() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, Constants.FAST_CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}