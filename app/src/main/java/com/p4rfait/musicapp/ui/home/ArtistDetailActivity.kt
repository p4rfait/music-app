package com.p4rfait.musicapp.ui.home

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.p4rfait.musicapp.R

class ArtistDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail)

        val nameTextView = findViewById<TextView>(R.id.artistName)
        val bioTextView = findViewById<TextView>(R.id.artistBio)
        val songsTextView = findViewById<TextView>(R.id.artistSongs)

        intent?.extras?.let {
            val artistName = it.getString("artist_name") ?: "No name"
            nameTextView.text = artistName
            bioTextView.text = it.getString("artist_bio") ?: "No bio"

            val artist = listOf(
                Artist("Artist 1", "Pop", "Bio 1", listOf("Song 1", "Song 2")),
                Artist("Artist 2", "Rock", "Bio 2", listOf("Song 3", "Song 4")),
                Artist("Artist 3", "Jazz", "Bio 3", listOf("Song 5", "Song 6"))
            ).find { it.name == artistName }
            songsTextView.text = artist?.songs?.joinToString("\n") ?: "No songs"
        }
    }
}