package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //переменные VIEW===========================================================================
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val button_share = findViewById<Button>(R.id.button_share)
        val button_support = findViewById<Button>(R.id.button_support)
        val button_license = findViewById<Button>(R.id.button_license)
        val settings_back_button = findViewById<ImageButton>(R.id.settings_back_button)

        //основной листинг
        if ((applicationContext as App).darkTheme) {
            themeSwitcher.isChecked = true
        }


        //слушатели нажатий=========================================================================
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        button_share.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            val chooser = Intent.createChooser(shareIntent, null)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_message_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            startActivity(chooser)
        }

        button_support.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.letter_text_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.letter_text))
            startActivity(supportIntent)
        }

        button_license.setOnClickListener{
            val licenseIntent = Intent(Intent.ACTION_VIEW)
            licenseIntent.data = Uri.parse(getString(R.string.license_url))
            startActivity(licenseIntent)
        }



        settings_back_button.setOnClickListener{
            val settingsBackIntent = Intent(this, MainActivity::class.java)
            startActivity(settingsBackIntent)
        }
    }

}