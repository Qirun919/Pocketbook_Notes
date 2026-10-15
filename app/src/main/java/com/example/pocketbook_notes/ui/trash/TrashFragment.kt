package com.example.pocketbook_notes.ui.trash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketbook_notes.databinding.FragmentTrashBinding
import com.example.pocketbook_notes.ui.adapter.TrashAdapter
import kotlinx.coroutines.launch

class TrashFragment : Fragment() {

    private val viewModel: TrashViewModel by viewModels {
        TrashViewModel.factory
    }

    private lateinit var binding: FragmentTrashBinding

    private lateinit var trashAdapter: TrashAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeNotes()
        setupButtons()
    }

    private fun setupRecyclerView() {
        trashAdapter = TrashAdapter(
            onRestore = { note ->
                viewModel.restore(note)
            },
            onDelete = { note ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete forever?")
                    .setMessage("This note can't be recovered.")
                    .setPositiveButton("Delete") { _, _ -> viewModel.deleteForever(note) }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        binding.trashRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trashAdapter
        }
    }

    private fun observeNotes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notes.collect { notes ->
                trashAdapter.submitList(notes)
            }
        }
    }

    private fun setupButtons() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.emptyTrashButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Empty trash?")
                .setMessage("All notes in trash will be deleted forever.")
                .setPositiveButton("Empty") { _, _ -> viewModel.emptyTrash() }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}