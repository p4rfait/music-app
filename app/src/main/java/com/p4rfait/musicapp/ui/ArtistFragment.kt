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
import com.p4rfait.musicapp.data.Artist
import com.p4rfait.musicapp.databinding.FragmentArtistBinding
import com.p4rfait.musicapp.ui.common.ArtistAdapter

class ArtistFragment : Fragment() {

    private var _binding: FragmentArtistBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { FirebaseFirestore.getInstance() }
    private var artistsReg: ListenerRegistration? = null

    private val allArtists = mutableListOf<Artist>()
    private val adapter by lazy { ArtistAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentArtistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.artistRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ArtistFragment.adapter
            setHasFixedSize(true)
        }

        binding.addArtistFab.setOnClickListener {
            findNavController().navigate(R.id.action_artist_to_editArtist)
        }

        binding.artistSearchEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { applyFilter(s?.toString()) }
        })

        listenArtists()
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
                allArtists.clear()
                snapshot?.documents?.forEach { d ->
                    val obj = d.toObject(Artist::class.java)
                    if (obj != null) {
                        val item = Artist(
                            id = d.id,
                            name = obj.name,
                            country = obj.country
                        )
                        allArtists.add(item)
                    }
                }
                applyFilter(b.artistSearchEdit.text?.toString())
            }
    }

    private fun applyFilter(query: String?) {
        val q = query?.trim()?.lowercase().orEmpty()
        val list = if (q.isBlank()) allArtists else allArtists.filter {
            it.name.lowercase().contains(q) || it.country.lowercase().contains(q)
        }
        adapter.submitList(list.toList())
    }

    override fun onDestroyView() {
        artistsReg?.remove()
        artistsReg = null
        _binding = null
        super.onDestroyView()
    }
}
