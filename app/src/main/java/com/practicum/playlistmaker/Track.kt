package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String
)