package com.p4rfait.musicapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.data.Album
import com.p4rfait.musicapp.data.Artist
import com.p4rfait.musicapp.data.Song
import com.p4rfait.musicapp.databinding.FragmentMusicBinding
import com.p4rfait.musicapp.ui.common.SongAdapter

class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { FirebaseFirestore.getInstance() }
    private var songsReg: ListenerRegistration? = null
    private var albumsReg: ListenerRegistration? = null
    private var artistsReg: ListenerRegistration? = null

    private val allSongs = mutableListOf<Song>()
    private val artistNameById = mutableMapOf<String, String>()
    private val albumTitleById = mutableMapOf<String, String>()

    private val adapter by lazy {
        SongAdapter(
            albumTitleOf = { id -> albumTitleById[id] ?: "" },
            artistNameOf = { id -> artistNameById[id] ?: "" }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.songRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MusicFragment.adapter
            setHasFixedSize(true)
        }

        binding.addSongFab.setOnClickListener {
            findNavController().navigate(R.id.action_music_to_editSong)
        }

        binding.songSearchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { applyFilter(s?.toString()) }
        })

        listenArtists()
        listenAlbums()
        listenSongs()
    }

    private fun listenArtists() {
        artistsReg?.remove()
        artistsReg = db.collection("artists")
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                val b = _binding ?: return@addSnapshotListener
                if (e != null) {
                    Snackbar.make(b.root, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                artistNameById.clear()
                snapshot?.documents?.forEach { d ->
                    val obj = d.toObject(Artist::class.java)
                    if (obj != null) artistNameById[d.id] = obj.name
                }
                applyFilter(b.songSearchEdit.text?.toString())
            }
    }

    private fun listenAlbums() {
        albumsReg?.remove()
        albumsReg = db.collection("albums")
            .orderBy("title", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                val b = _binding ?: return@addSnapshotListener
                if (e != null) {
                    Snackbar.make(b.root, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                albumTitleById.clear()
                snapshot?.documents?.forEach { d ->
                    val obj = d.toObject(Album::class.java)
                    if (obj != null) albumTitleById[d.id] = obj.title
                }
                applyFilter(b.songSearchEdit.text?.toString())
            }
    }

    private fun listenSongs() {
        songsReg?.remove()
        songsReg = db.collection("songs")
            .orderBy("title", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                val b = _binding ?: return@addSnapshotListener
                if (e != null) {
                    Snackbar.make(b.root, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                allSongs.clear()
                snapshot?.documents?.forEach { d ->
                    val obj = d.toObject(Song::class.java)
                    if (obj != null) {
                        val item = Song(
                            id = d.id,
                            title = obj.title,
                            durationSec = obj.durationSec,
                            albumId = obj.albumId,
                            artistId = obj.artistId
                        )
                        allSongs.add(item)
                    }
                }
                applyFilter(b.songSearchEdit.text?.toString())
            }
    }

    private fun applyFilter(query: String?) {
        val q = query?.trim()?.lowercase().orEmpty()
        val list = if (q.isBlank()) allSongs else allSongs.filter { s ->
            s.title.lowercase().contains(q) ||
                    s.durationSec.toString().contains(q) ||
                    albumTitleById[s.albumId].orEmpty().lowercase().contains(q) ||
                    artistNameById[s.artistId].orEmpty().lowercase().contains(q)
        }
        adapter.submitList(list.toList())
    }

    override fun onDestroyView() {
        songsReg?.remove(); songsReg = null
        albumsReg?.remove(); albumsReg = null
        artistsReg?.remove(); artistsReg = null
        _binding = null
        super.onDestroyView()
    }
}
