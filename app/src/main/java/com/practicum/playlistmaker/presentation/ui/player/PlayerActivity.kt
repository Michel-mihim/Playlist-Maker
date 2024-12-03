package com.practicum.playlistmaker.presentation.ui.player

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.domain.searchTracks.models.PlayerStatus
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.converters.dimensionsFloatToIntConvert
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val SHOW_PROGRESS_DELAY = 500L
    }
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

    //VALS
    private val handler = Handler(Looper.getMainLooper())
    private val showProgressRunnable = Runnable { showProgressState() }

    //VARS
    private var mediaPlayer = MediaPlayer()

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

        preparePlayer(bundle?.getString("b_previewUrl"))

        //слушатели нажатий

        playerBackButton.setOnClickListener{
            finish()
        }

        trackPlayButton.setOnClickListener {
            playbackControl()
        }
    }

    private fun showProgressState(){
        trackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        handler.postDelayed(showProgressRunnable, SHOW_PROGRESS_DELAY)
    }

    private fun preparePlayer(url: String?){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener{
            trackPlayButton.isEnabled = true
            playerStatus = PlayerStatus.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener{
            trackPlayButton.setImageResource(R.drawable.track_play)
            playerStatus = PlayerStatus.STATE_PREPARED
            handler.removeCallbacks(showProgressRunnable)
            trackProgress.text = "00:00"
        }

    }

    private fun startPlayer(){
        mediaPlayer.start()
        trackPlayButton.setImageResource(R.drawable.track_pause)
        playerStatus = PlayerStatus.STATE_PLAYING
        handler.post(showProgressRunnable)
    }

    private fun pausePlayer(){
        mediaPlayer.pause()
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
        mediaPlayer.release()
    }
}