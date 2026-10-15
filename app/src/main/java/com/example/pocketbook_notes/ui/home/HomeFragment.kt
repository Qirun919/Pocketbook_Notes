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
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pocketbook_notes.R

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.factory
    }

    private lateinit var binding: FragmentHomeBinding

    private lateinit var  noteAdapter: NoteAdapter

    private var selectedColour = ""

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
        setupSearch()
        setupColourFilter()
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

        // up and down filter
        binding.sortButton.setOnClickListener {
            val newestFirst = viewModel.toggleSort()
            binding.sortLabel.text = if (newestFirst) {
                "Newest first · tap ⇅ for oldest first"
            } else {
                "Oldest first · tap ⇅ for newest first"
            }
        }
    }


    // search
    private fun setupSearch() {
        binding.searchInput.doAfterTextChanged { text ->
            viewModel.setSearch(text.toString().trim())
        }
    }

    // color
    private fun setupColourFilter() {
        val colourButtons = mapOf(
            "blue" to binding.filterBlue,
            "green" to binding.filterGreen,
            "yellow" to binding.filterYellow,
            "peach" to binding.filterPeach,
            "pink" to binding.filterPink,
            "lavender" to binding.filterLavender
        )

        for ((name, button) in colourButtons) {
            button.setOnClickListener {
                // tap one more time it go back to all
                selectedColour = if (selectedColour == name) "" else name
                viewModel.setColour(selectedColour)
                updateColourFilter()
            }
        }

        binding.filterAll.setOnClickListener {
            selectedColour = ""
            viewModel.setColour("")
            updateColourFilter()
        }

        updateColourFilter()
    }

    private fun updateColourFilter() {
        val colourButtons = mapOf(
            "blue" to binding.filterBlue,
            "green" to binding.filterGreen,
            "yellow" to binding.filterYellow,
            "peach" to binding.filterPeach,
            "pink" to binding.filterPink,
            "lavender" to binding.filterLavender
        )

        val selectedWidth = (3 * resources.displayMetrics.density).toInt()

        for ((name, button) in colourButtons) {
            button.strokeWidth = if (name == selectedColour) selectedWidth else 0
        }

        binding.filterAll.alpha = if (selectedColour.isEmpty()) 1f else 0.4f
    }
}
