package com.p4rfait.musicapp.ui.songs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.p4rfait.musicapp.databinding.FragmentSongsBinding
import com.p4rfait.musicapp.model.Song

class SongsFragment : Fragment() {

    private var _binding: FragmentSongsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val songs = listOf(
            Song("Canción 1", "3:45"),
            Song("Canción 2", "4:12"),
            Song("Canción 3", "5:01")
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
