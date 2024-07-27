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

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //sharing
        val button_share = findViewById<Button>(R.id.button_share)
        button_share.setOnClickListener{
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_message_subject))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            startActivity(shareIntent)
        }

        //support
        val button_support = findViewById<Button>(R.id.button_support)
        button_support.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.student_email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.letter_text_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.letter_text))
            startActivity(supportIntent)
        }

        //license
        val button_license = findViewById<Button>(R.id.button_license)
        button_license.setOnClickListener{
            val licenseIntent = Intent(Intent.ACTION_VIEW)
            licenseIntent.data = Uri.parse(getString(R.string.license_url))
            startActivity(licenseIntent)
        }
    }

}