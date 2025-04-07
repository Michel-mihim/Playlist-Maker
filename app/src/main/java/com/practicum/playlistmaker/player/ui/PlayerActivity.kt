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
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.domain.models.PlayerActivityState
import com.practicum.playlistmaker.search.domain.models.SearchActivityState
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.utils.converters.dimensionsFloatToIntConvert
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    //VAL BY
    private val playerViewModel by viewModel<PlayerViewModel>()

    //основной листинг==============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        playerViewModel.observePlayerActivityCurrentState().observe(this) {
            renderPlayerState(it)
        }

        binding.buttonPlay2.isEnabled = false //пока плеер не готов на нее нельзя нажимать

        //основной листинг
        val bundle = intent.extras
        if (bundle != null) {
            binding.trackPlayerName.text = bundle.getString(Constants.TRACK_NAME_KEY)
            binding.trackArtistName.text = bundle.getString(Constants.ARTIST_NAME_KEY)
            binding.attr12Time.text = bundle.getString(Constants.TRACK_TIME_KEY)
            binding.attr22Album.text = bundle.getString(Constants.TRACK_ALBUM_KEY)
            binding.attr32Year.text = bundle.getString(Constants.TRACK_YEAR_KEY)
            binding.attr42Genre.text = bundle.getString(Constants.TRACK_GENRE_KEY)
            binding.attr52Country.text = bundle.getString(Constants.TRACK_COUNTRY_KEY)

            val cornerDp = resources.getDimension(R.dimen.track_poster_corner)
            val cornerPx = dimensionsFloatToIntConvert(cornerDp, this)
            Glide.with(this)
                .load(bundle.getString(Constants.PIC_URL_KEY))
                .placeholder(R.drawable.placeholder_large)
                .transform(RoundedCorners(cornerPx))
                .into(binding.playerTrackImage)
        }

        playerViewModel.mediaPlayerPrepare(bundle?.getString((Constants.PREVIEW_PIC_URL_KEY)))

        //слушатели нажатий
        binding.playerBackButton.setOnClickListener{
            finish()
        }

        binding.buttonPlay2.setOnClickListener {
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
        binding.buttonPlay2.isEnabled = ready
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
        binding.buttonPlay2.setImageResource(R.drawable.track_play)
    }

    private fun setTrackPlayButtonPause() {
        binding.buttonPlay2.setImageResource(R.drawable.track_pause)
    }

    private fun setTrackProgress(progress: String) {
        binding.trackPlayerProgress.text = progress
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