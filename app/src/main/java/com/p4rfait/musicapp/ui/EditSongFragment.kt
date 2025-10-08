package com.p4rfait.musicapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.data.Album
import com.p4rfait.musicapp.data.Artist
import com.p4rfait.musicapp.databinding.FragmentEditSongBinding

class EditSongFragment : Fragment() {

    private var _b: FragmentEditSongBinding? = null
    private val b get() = _b!!
    private val db by lazy { FirebaseFirestore.getInstance() }

    private var artists: List<Pair<String, String>> = emptyList()
    private var albums: List<Triple<String, String, String>> = emptyList()
    private var selectedArtistId: String? = null
    private var selectedAlbumId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentEditSongBinding.inflate(inflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db.collection("artists").orderBy("name", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { snap ->
                artists = snap.documents.mapNotNull { d ->
                    d.toObject(Artist::class.java)?.let { d.id to it.name }
                }
                b.artistDrop.setAdapter(
                    ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, artists.map { it.second })
                )
            }
            .addOnFailureListener { e ->
                Snackbar.make(view, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
            }

        db.collection("albums").orderBy("title", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { snap ->
                albums = snap.documents.mapNotNull { d ->
                    d.toObject(Album::class.java)?.let { Triple(d.id, it.title, it.artistId) }
                }
                refreshAlbumDropdown()
            }
            .addOnFailureListener { e ->
                Snackbar.make(view, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
            }

        b.artistDrop.setOnItemClickListener { _, _, pos, _ ->
            selectedArtistId = artists.getOrNull(pos)?.first
            selectedAlbumId = null
            b.albumDrop.text = null
            refreshAlbumDropdown()
        }
        b.albumDrop.setOnItemClickListener { _, _, pos, _ ->
            selectedAlbumId = currentAlbumsForArtist().getOrNull(pos)?.first
        }

        b.saveBtn.setOnClickListener {
            val title = b.titleEdit.text?.toString()?.trim().orEmpty()
            val duration = b.durationEdit.text?.toString()?.toIntOrNull() ?: 0
            val artistId = selectedArtistId
            val albumId = selectedAlbumId

            if (title.isBlank() || duration <= 0 || artistId.isNullOrBlank() || albumId.isNullOrBlank()) {
                Snackbar.make(view, R.string.validate_fields, Snackbar.LENGTH_SHORT).show(); return@setOnClickListener
            }

            db.collection("songs").add(
                mapOf("title" to title, "durationSec" to duration, "artistId" to artistId, "albumId" to albumId)
            ).addOnSuccessListener {
                Snackbar.make(view, R.string.saved, Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.addOnFailureListener { e ->
                Snackbar.make(view, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun currentAlbumsForArtist(): List<Pair<String, String>> {
        val aid = selectedArtistId
        return if (aid.isNullOrBlank()) albums.map { it.first to it.second }
        else albums.filter { it.third == aid }.map { it.first to it.second }
    }

    private fun refreshAlbumDropdown() {
        val list = currentAlbumsForArtist()
        b.albumDrop.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list.map { it.second })
        )
    }

    override fun onDestroyView() { _b = null; super.onDestroyView() }
}
