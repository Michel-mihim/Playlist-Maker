package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track

class TracksAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    var onItemClickListener: ((Track) -> Unit)? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        //слушатель нажатия на трек в результатах поиска
        holder.itemView.setOnClickListener {
            //передача трека в активити
            val track: Track = tracks[position]
            onItemClickListener?.invoke(track)
        }
    }

    override fun getItemCount() = tracks.size

    interface TrackClickListener {
        fun onTrackClick(track: Track)
        fun onLikeClick(track: Track)
    }

}