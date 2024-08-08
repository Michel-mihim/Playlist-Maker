package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView

data class SearchResultBox(val track_name: String, val artist_name: String)

class SearchResultBoxAdapter(
    private val searchResultBoxes: List<SearchResultBox>
) : RecyclerView.Adapter<SearchResultBoxAdapter.SearchResultViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_result_box_view, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(searchResultBoxes[position])
    }

    override fun getItemCount() = searchResultBoxes.size


    class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackNameView: TextView
        private val artistNameView: TextView

        init {
            trackNameView = itemView.findViewById(R.id.track_name)
            artistNameView = itemView.findViewById(R.id.artist_name)
        }

        fun bind(searchResultBox: SearchResultBox) {
            trackNameView.text = searchResultBox.track_name
            artistNameView.text = searchResultBox.artist_name
        }

    }
}