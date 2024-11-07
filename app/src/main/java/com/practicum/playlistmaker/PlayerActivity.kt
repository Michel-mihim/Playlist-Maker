package com.practicum.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {

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
    private lateinit var playButton2: Button

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
        //playButton2 = findViewById(R.id.button_play_2)
        Log.d("wtf", "point")
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


            Glide.with(this)
                .load(bundle.getString("b_artworkUrl100"))
                .placeholder(R.drawable.placeholder_large)
                .centerInside()
                .into(playerTrackImage)
        }

        //preparePlayer(bundle?.getString("b_previewUrl"))

        //слушатели нажатий

        playerBackButton.setOnClickListener{
            finish()
        }
    }


    private fun preparePlayer(url: String?){
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener{
            playButton2.isEnabled = true
            playerStatus = PlayerStatus.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener{
            playButton2.background = getDrawable(R.drawable.track_play)
            playerStatus = PlayerStatus.STATE_PREPARED
        }
    }
}