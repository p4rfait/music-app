package com.p4rfait.musicapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.p4rfait.musicapp.databinding.FragmentArtistsBinding
import android.content.Intent

class ArtistsFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.artistsList
        recyclerView.layoutManager = LinearLayoutManager(context)

        val artists = listOf(
            Artist("Artist 1", "Pop", "Bio 1", listOf("Song 1", "Song 2")),
            Artist("Artist 2", "Rock", "Bio 2", listOf("Song 3", "Song 4")),
            Artist("Artist 3", "Jazz", "Bio 3", listOf("Song 5", "Song 6"))
        )

        val adapter = ArtistAdapter(artists, requireContext()) { artist ->
            val intent = Intent(requireContext(), ArtistDetailActivity::class.java).apply {
                putExtra("artist_name", artist.name)
                putExtra("artist_bio", artist.bio)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        val searchView: EditText = binding.searchArtists
        searchView.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val filteredList = artists.filter { it.name.contains(s.toString(), ignoreCase = true) }
                recyclerView.adapter = ArtistAdapter(filteredList, requireContext()) { artist ->
                    val intent = Intent(requireContext(), ArtistDetailActivity::class.java).apply {
                        putExtra("artist_name", artist.name)
                        putExtra("artist_bio", artist.bio)
                    }
                    startActivity(intent)
                }
            }
        })

        val genreSpinner: Spinner = binding.filterGenre
        val genres = arrayOf("All", "Pop", "Rock", "Jazz")
        genreSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genres)
        genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGenre = genres[position]
                val filteredList = if (selectedGenre == "All") artists else artists.filter { it.genre == selectedGenre }
                recyclerView.adapter = ArtistAdapter(filteredList, requireContext()) { artist ->
                    val intent = Intent(requireContext(), ArtistDetailActivity::class.java).apply {
                        putExtra("artist_name", artist.name)
                        putExtra("artist_bio", artist.bio)
                    }
                    startActivity(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}