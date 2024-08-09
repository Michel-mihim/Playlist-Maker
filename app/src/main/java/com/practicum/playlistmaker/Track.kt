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

class TrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size


    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackNameView: TextView
        private val artistNameView: TextView
        private val trackTimeView: TextView
        private val trackImageView: ImageView

        init {
            trackNameView = itemView.findViewById(R.id.track_name)
            artistNameView = itemView.findViewById(R.id.artist_name)
            trackTimeView = itemView.findViewById(R.id.track_time)
            trackImageView = itemView.findViewById(R.id.trackImage)
        }

        fun bind(track: Track) {
            trackNameView.text = track.trackName
            artistNameView.text = track.artistName
            trackTimeView.text = track.trackTime

            //val cornerDp = (R.dimen.track_image_corner)
            //val cornerPx = dpToPx(R.dimen.track_image_corner, itemView.context)
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.empty_music_image)
                .centerInside()
                .transform(RoundedCorners(R.dimen.track_image_corner))
                .into(trackImageView)
        }

        //не понял как использовать эту функцию, он требует на вход FLOAT, а в dimens INT
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }

    }
}