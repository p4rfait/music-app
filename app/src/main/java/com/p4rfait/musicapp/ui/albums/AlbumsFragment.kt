package com.p4rfait.musicapp.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.databinding.FragmentAlbumsBinding
import com.p4rfait.musicapp.model.Album

class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val albums = listOf(
            Album("Álbum 1", "Artista 1", R.drawable.ic_album_placeholder),
            Album("Álbum 2", "Artista 2", R.drawable.ic_album_placeholder),
            Album("Álbum 3", "Artista 3", R.drawable.ic_album_placeholder)
        )


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
