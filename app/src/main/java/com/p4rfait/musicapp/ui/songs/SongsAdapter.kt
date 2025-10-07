package com.p4rfait.musicapp.ui.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.model.Song

class SongsAdapter(private val songs: List<Song>) :
    RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.text_song_title)
        val duration: TextView = view.findViewById(R.id.text_song_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song, parent, false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.title.text = song.title
        holder.duration.text = song.duration
    }

    override fun getItemCount(): Int = songs.size
}
