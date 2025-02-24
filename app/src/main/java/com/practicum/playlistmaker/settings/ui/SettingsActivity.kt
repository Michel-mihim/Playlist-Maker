package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.utils.constants.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var buttonShare: Button
    private lateinit var buttonSupport: Button
    private lateinit var buttonLicense: Button
    private lateinit var settingsBackButton: ImageButton

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //переменные VIEW===========================================================================
        themeSwitcher = findViewById(R.id.theme_switcher)
        buttonShare = findViewById(R.id.button_share)
        buttonSupport = findViewById(R.id.button_support)
        buttonLicense = findViewById(R.id.button_license)
        settingsBackButton = findViewById(R.id.settings_back_button)


        settingsViewModel.observeSettingsActivityTheme().observe(this) { isDark ->
            darkThemeSwitcherActivated(isDark)
        }

        settingsViewModel.observeShareActivityIntentLiveData().observe(this) {intent ->
            startActivity(intent)
        }

        settingsViewModel.observeSupportEmailActivityIntentLiveData().observe(this) {intent ->
            Log.d("wtf", "Send intent before starting." + intent.toString())
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this@SettingsActivity, Constants.EMAIL_CLIENT_NOT_FOUND, Toast.LENGTH_LONG).show()
            }

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
            Log.d("wtf", "Send intent asked.")
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