package com.p4rfait.musicapp.ui.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.model.Album

class AlbumAdapter(private val albums: List<Album>) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.text_album_name)
        val artist: TextView = view.findViewById(R.id.text_album_artist)
        val cover: ImageView = view.findViewById(R.id.image_album_cover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.name.text = album.name
        holder.artist.text = album.artist
        holder.cover.setImageResource(album.coverRes)
    }

    override fun getItemCount(): Int = albums.size
}
