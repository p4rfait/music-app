package com.p4rfait.musicapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.p4rfait.musicapp.databinding.FragmentHomeBinding
import com.p4rfait.musicapp.R
=======
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.p4rfait.musicapp.databinding.FragmentHomeBinding
>>>>>>> 74fc9fcd8601c12b6a8cf6b7685e813debfc4e4b

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
<<<<<<< HEAD
=======

    // This property is only valid between onCreateView and
    // onDestroyView.
>>>>>>> 74fc9fcd8601c12b6a8cf6b7685e813debfc4e4b
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
<<<<<<< HEAD
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonArtists.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_artists)
        }
=======
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
>>>>>>> 74fc9fcd8601c12b6a8cf6b7685e813debfc4e4b
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}