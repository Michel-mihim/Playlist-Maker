package com.practicum.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.PREFERENCES
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //переменные VIEW===========================================================================
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)
        val buttonShare = findViewById<Button>(R.id.button_share)
        val buttonSupport = findViewById<Button>(R.id.button_support)
        val buttonLicense = findViewById<Button>(R.id.button_license)
        val settingsBackButton = findViewById<ImageButton>(R.id.settings_back_button)

        //основной листинг
        if ((applicationContext as App).darkTheme) {
            themeSwitcher.isChecked = true
        }

        //слушатели нажатий=========================================================================
        themeSwitcher.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme((applicationContext as App)
                .getSharedPreferences(PREFERENCES, MODE_PRIVATE), checked)
        }

        buttonShare.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            val chooser = Intent.createChooser(shareIntent, null)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_message_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            startActivity(chooser)
        }

        buttonSupport.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.letter_text_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.letter_text))
            startActivity(supportIntent)
        }

        buttonLicense.setOnClickListener{
            val licenseIntent = Intent(Intent.ACTION_VIEW)
            licenseIntent.data = Uri.parse(getString(R.string.license_url))
            startActivity(licenseIntent)
        }

        settingsBackButton.setOnClickListener{
            finish()
        }
    }

}