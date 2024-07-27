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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.share_message_subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.share_message)
            //shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Полезная ссылка:")
            //shareIntent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            startActivity(shareIntent)
        }

        //support
        val button_support = findViewById<Button>(R.id.button_support)
        button_support.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ya-mihim@yandex.ru"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
            supportIntent.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            startActivity(supportIntent)
        }

        //license
        val button_license = findViewById<Button>(R.id.button_license)
        button_license.setOnClickListener{
            val licenseIntent = Intent(Intent.ACTION_VIEW)
            licenseIntent.data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(licenseIntent)
        }
    }

}