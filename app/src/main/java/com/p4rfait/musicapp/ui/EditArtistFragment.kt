package com.p4rfait.musicapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.p4rfait.musicapp.R
import com.p4rfait.musicapp.databinding.FragmentEditArtistBinding
import androidx.navigation.fragment.findNavController

class EditArtistFragment : Fragment() {

    private var _b: FragmentEditArtistBinding? = null
    private val b get() = _b!!
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentEditArtistBinding.inflate(inflater, container, false).also { _b = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        b.saveBtn.setOnClickListener {
            val name = b.nameEdit.text?.toString()?.trim().orEmpty()
            val country = b.countryEdit.text?.toString()?.trim().orEmpty()
            if (name.isBlank()) {
                Snackbar.make(view, R.string.validate_fields, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db.collection("artists").add(mapOf(
                "name" to name,
                "country" to country
            )).addOnSuccessListener {
                Snackbar.make(view, R.string.saved, Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.addOnFailureListener { e ->
                Snackbar.make(view, getString(R.string.generic_error, e.message), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() { _b = null; super.onDestroyView() }
}
