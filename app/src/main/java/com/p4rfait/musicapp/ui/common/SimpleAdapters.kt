package com.p4rfait.musicapp.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.data.Album
import com.p4rfait.musicapp.data.Artist
import com.p4rfait.musicapp.data.Song

private class ArtistDiff : DiffUtil.ItemCallback<Artist>() {
    override fun areItemsTheSame(oldItem: Artist, newItem: Artist) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Artist, newItem: Artist) = oldItem == newItem
}

class ArtistAdapter :
    ListAdapter<Artist, ArtistVH>(ArtistDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistVH(v)
    }
    override fun onBindViewHolder(holder: ArtistVH, position: Int) {
        val a = getItem(position)
        holder.title.text = a.name
        holder.sub.text = a.country
    }
}

class ArtistVH(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val sub: TextView = view.findViewById(R.id.sub)
}

private class AlbumDiff : DiffUtil.ItemCallback<Album>() {
    override fun areItemsTheSame(oldItem: Album, newItem: Album) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Album, newItem: Album) = oldItem == newItem
}

class AlbumAdapter(
    private val artistNameOf: (String) -> String = { "" }
) : ListAdapter<Album, AlbumVH>(AlbumDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumVH(v)
    }
    override fun onBindViewHolder(holder: AlbumVH, position: Int) {
        val a = getItem(position)
        holder.title.text = a.title
        holder.sub.text = holder.itemView.context.getString(
            R.string.album_sub, a.year, artistNameOf(a.artistId)
        )
    }
}

class AlbumVH(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val sub: TextView = view.findViewById(R.id.sub)
}

private class SongDiff : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
}

class SongAdapter(
    private val albumTitleOf: (String) -> String = { "" },
    private val artistNameOf: (String) -> String = { "" }
) : ListAdapter<Song, SongVH>(SongDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false)
        return SongVH(v)
    }
    override fun onBindViewHolder(holder: SongVH, position: Int) {
        val s = getItem(position)
        holder.title.text = s.title
        holder.sub.text = holder.itemView.context.getString(
            R.string.song_sub, s.durationSec, albumTitleOf(s.albumId), artistNameOf(s.artistId)
        )
    }
}

class SongVH(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.title)
    val sub: TextView = view.findViewById(R.id.sub)
}
