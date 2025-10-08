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
import com.p4rfait.musicapp.data.Artist
import com.p4rfait.musicapp.databinding.FragmentEditAlbumBinding

class EditAlbumFragment : Fragment() {

    private var _b: FragmentEditAlbumBinding? = null
    private val b get() = _b!!
    private val db by lazy { FirebaseFirestore.getInstance() }

    private var artists: List<Pair<String, String>> = emptyList() // (id, name)
    private var selectedArtistId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _b = FragmentEditAlbumBinding.inflate(inflater, container, false)
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

        b.artistDrop.setOnItemClickListener { _, _, pos, _ ->
            selectedArtistId = artists.getOrNull(pos)?.first
        }

        b.saveBtn.setOnClickListener {
            val title = b.titleEdit.text?.toString()?.trim().orEmpty()
            val year = b.yearEdit.text?.toString()?.toIntOrNull() ?: 0
            val artistId = selectedArtistId

            if (title.isBlank() || year <= 0 || artistId.isNullOrBlank()) {
                Snackbar.make(view, R.string.validate_fields, Snackbar.LENGTH_SHORT).show(); return@setOnClickListener
            }

            db.collection("albums").add(
                mapOf("title" to title, "year" to year, "artistId" to artistId)
            ).addOnSuccessListener {
                Snackbar.make(view, R.string.saved, Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.addOnFailureListener { e ->
                Snackbar.make(view, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() { _b = null; super.onDestroyView() }
}
