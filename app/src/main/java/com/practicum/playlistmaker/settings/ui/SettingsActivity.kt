package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var buttonShare: Button
    private lateinit var buttonSupport: Button
    private lateinit var buttonLicense: Button
    private lateinit var settingsBackButton: ImageButton

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //переменные VIEW===========================================================================
        themeSwitcher = findViewById(R.id.theme_switcher)
        buttonShare = findViewById(R.id.button_share)
        buttonSupport = findViewById(R.id.button_support)
        buttonLicense = findViewById(R.id.button_license)
        settingsBackButton = findViewById(R.id.settings_back_button)

        settingsViewModel = ViewModelProvider(this, SettingsViewModel.getSettingsViewModelFactory())[SettingsViewModel::class.java]

        settingsViewModel.observeSettingsActivityTheme().observe(this) { isDark ->
            darkThemeSwitcherActivated(isDark)
        }

        settingsViewModel.observeShareActivityIntentLiveData().observe(this) {intent ->
            startActivity(intent)
        }

        settingsViewModel.observeSupportEmailActivityIntentLiveData().observe(this) {intent ->
            startActivity(intent)
        }

        settingsViewModel.observeTermsIntentLiveData().observe(this) { intent ->
            startActivity(intent)
        }

        //слушатели нажатий=========================================================================
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }

        buttonShare.setOnClickListener{
            settingsViewModel.shareApp()
        }

        buttonSupport.setOnClickListener{
            settingsViewModel.openSupport()
        }

        buttonLicense.setOnClickListener{
            settingsViewModel.openTerms()
        }

        settingsBackButton.setOnClickListener{
            finish()
        }
    }

    private fun darkThemeSwitcherActivated(isDark: Boolean) {
        themeSwitcher.isChecked = isDark
    }

}