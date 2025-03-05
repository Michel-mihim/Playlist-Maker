package com.practicum.playlistmaker.lib.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibBinding

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
               0 -> tab.text = "Избранные треки"
               1 -> tab.text = "Плейлисты"
            }

            tabMediator.attach()

        }


        val libBackButton = findViewById<ImageButton>(R.id.lib_back_button)
        libBackButton.setOnClickListener{
            finish()
        }

    }


    override fun onDestroy() {
        super.onDestroy()

        tabMediator.detach()
    }
}