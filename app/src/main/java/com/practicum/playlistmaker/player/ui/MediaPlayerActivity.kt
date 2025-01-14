package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.player.domain.models.PlayerStatus
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.practicum.playlistmaker.utils.constants.Constants
import com.practicum.playlistmaker.utils.converters.dimensionsFloatToIntConvert


class MediaPlayerActivity : AppCompatActivity() {

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

    //LATEINIT VARS
    private lateinit var mediaPlayerInteractor: MediaPlayerInteractor

    //VALS
    private val handler = Handler(Looper.getMainLooper())
    private val showProgressRunnable = Runnable { showProgress() }

    //VARS
    private var playerStatus: PlayerStatus = PlayerStatus.STATE_DEFAULT

    //основной листинг==============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            playerTrackName.text = bundle.getString("b_track_name")
            playerArtistName.text = bundle.getString("b_artist_name")
            playerTrackTime.text = bundle.getString("b_track_time")
            playerTrackAlbum.text = bundle.getString("b_track_album")
            playerTrackYear.text = bundle.getString("b_track_year")
            playerTrackGenre.text = bundle.getString("b_track_genre")
            playerTrackCountry.text = bundle.getString("b_track_country")

            val cornerDp = resources.getDimension(R.dimen.track_poster_corner)
            val cornerPx = dimensionsFloatToIntConvert(cornerDp, this)
            Glide.with(this)
                .load(bundle.getString("b_artworkUrl100"))
                .placeholder(R.drawable.placeholder_large)
                .transform(RoundedCorners(cornerPx))
                .into(playerTrackImage)
        }

        mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
        mediaPlayerInteractor.prepare(
            bundle?.getString(("b_previewUrl")),
            onPrepared = { ->
                trackPlayButton.isEnabled = true
                playerStatus = PlayerStatus.STATE_PREPARED
            },
            onCompletion = { -> //окончание воспроизведения
                trackPlayButton.setImageResource(R.drawable.track_play)
                playerStatus = PlayerStatus.STATE_PREPARED
                handler.removeCallbacks(showProgressRunnable)
                trackProgress.text = Constants.TRACK_IS_OVER_PROGRESS
            }
        )

        //слушатели нажатий
        playerBackButton.setOnClickListener{
            finish()
        }

        trackPlayButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun showProgress(){
        mediaPlayerInteractor.timerUpdate(
            onTimerUpdated = { progress ->
                trackProgress.text = progress
            }
        )
        handler.postDelayed(showProgressRunnable, Constants.SHOW_PROGRESS_DELAY)
    }

    private fun startPlayer(){
        mediaPlayerInteractor.start()
        trackPlayButton.setImageResource(R.drawable.track_pause)
        playerStatus = PlayerStatus.STATE_PLAYING
        handler.post(showProgressRunnable)
    }

    private fun pausePlayer(){
        mediaPlayerInteractor.pause()
        trackPlayButton.setImageResource(R.drawable.track_play)
        playerStatus = PlayerStatus.STATE_PAUSED
        handler.removeCallbacks(showProgressRunnable)
    }

    private fun playbackControl() {
        when (playerStatus) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStatus.STATE_PREPARED -> {
                startPlayer()
            }
            PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerStatus.STATE_DEFAULT -> TODO()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(showProgressRunnable)
        mediaPlayerInteractor.release()
    }
}