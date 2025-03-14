package com.practicum.playlistmaker.lib.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibBinding
import com.practicum.playlistmaker.utils.constants.Constants

class LibActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.libViewPager.adapter = LibViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.libTablayout, binding.libViewPager) { tab, position ->
            when(position) {
               0 -> tab.text = getString(R.string.favourite_tracks)
               1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        //val libBackButton = findViewById<ImageButton>(R.id.lib_back_button)
        binding.libBackButton.setOnClickListener{
            finish()
        }

    }


    override fun onDestroy() {
        super.onDestroy()

        tabMediator.detach()
    }
}