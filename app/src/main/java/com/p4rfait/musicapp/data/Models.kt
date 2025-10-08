package com.p4rfait.musicapp.data

import com.google.firebase.Timestamp

data class Artist(
    val id: String = "",
    val name: String = "",
    val country: String = "",
    val createdAt: Timestamp? = null
)

data class Album(
    val id: String = "",
    val title: String = "",
    val year: Int = 0,
    val artistId: String = "",
    val createdAt: Timestamp? = null
)

data class Song(
    val id: String = "",
    val title: String = "",
    val durationSec: Int = 0,
    val albumId: String = "",
    val artistId: String = "",
    val createdAt: Timestamp? = null
)