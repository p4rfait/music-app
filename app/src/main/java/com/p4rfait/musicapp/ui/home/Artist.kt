package com.p4rfait.musicapp.ui.home

data class Artist(val name: String, val genre: String, val bio: String, val songs: List<String> = emptyList())