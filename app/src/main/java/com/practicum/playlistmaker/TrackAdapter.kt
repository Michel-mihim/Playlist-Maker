package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    var onItemClickListener: ((Track) -> Unit)? = null
    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        //слушатель нажатия на трек в результатах поиска
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                //передача трека в активити
                val track: Track = tracks[position]
                onItemClickListener?.invoke(track)

                Log.d("WTF", "Слушатель нажатия in adapter: "+track.trackName)
            }
        }
    }

    override fun getItemCount() = tracks.size


    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}