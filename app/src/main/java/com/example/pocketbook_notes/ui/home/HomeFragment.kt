package com.example.pocketbook_notes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketbook_notes.databinding.FragmentHomeBinding
import com.example.pocketbook_notes.ui.adapter.NoteAdapter
import kotlinx.coroutines.launch
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pocketbook_notes.R

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.factory
    }

    private lateinit var binding: FragmentHomeBinding

    private lateinit var  noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root

    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeNotes()
        setupButtons()
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter { note ->
            findNavController().navigate(
                R.id.action_home_to_detail,
                bundleOf("noteId" to note.id)
            )
        }

        binding.notesRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = noteAdapter
        }
    }

    private fun observeNotes() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                Log.d("HomeFragment", "Notes: $notes")

                noteAdapter.submitList(notes)
                binding.noteCountText.text = "${notes.size} notes"
            }
        }
    }


    private fun setupButtons() {

        binding.addNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_new)
        }

        binding.trashButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_trash)
        }
    }
}
