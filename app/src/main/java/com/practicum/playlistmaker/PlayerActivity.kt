package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackPlusLikeView: ImageButton
    private lateinit var playerBackButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        trackPlusLikeView = findViewById(R.id.button_plus_1)

        //trackPlusLikeView.setBackgroundColor(resources.getColor(android.R.color.transparent))

        playerBackButton = findViewById(R.id.player_back_button)
        playerBackButton.setOnClickListener {
            finish()
        }
    }
}