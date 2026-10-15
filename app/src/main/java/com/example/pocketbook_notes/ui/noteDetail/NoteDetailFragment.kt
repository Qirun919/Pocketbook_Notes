package com.example.pocketbook_notes.ui.noteDetail

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pocketbook_notes.R
import com.example.pocketbook_notes.data.model.Note
import com.example.pocketbook_notes.databinding.FragmentNoteDetailBinding
import kotlinx.coroutines.launch
import kotlin.jvm.java

class NoteDetailFragment : Fragment() {


    private val viewModel: NoteDetailViewModel by viewModels {
        NoteDetailViewModel.factory
    }

    private lateinit var binding: FragmentNoteDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // take the id
        val noteId = arguments?.getString("noteId") ?: return

        // use viewmodel to check the id is what data
        viewModel.getNote(noteId)
        observeNote()
        setupButtons(noteId)
    }

    private fun observeNote() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.note.collect { note ->
                if (note != null) {
                    showNote(note)
                }
            }
        }
    }

    private fun showNote(note: Note) {
        binding.detailTitle.text = note.title
        binding.detailBody.text = note.text
        binding.detailEditedDate.text = "Edited"

        // background with their own color
        binding.detailRoot.setBackgroundColor(
            ContextCompat.getColor(requireContext(), colourRes(note.colour))
        )
    }

    private fun setupButtons(noteId: String) {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.duplicateButton.setOnClickListener {
            val text = viewModel.getCopyText()
            if (text.isEmpty()) return@setOnClickListener

            val clipboard = requireContext().getSystemService(ClipboardManager::class.java)
            clipboard.setPrimaryClip(ClipData.newPlainText("note", text))

            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show()
        }

        binding.editButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_detail_to_edit,
                androidx.core.os.bundleOf("noteId" to noteId)
            )
        }

        binding.deleteButton.setOnClickListener {
            viewModel.moveToTrash(noteId)
            findNavController().popBackStack()
        }
    }

    private fun colourRes(colour: String): Int {
        return when (colour) {
            "blue" -> R.color.note_blue
            "green" -> R.color.note_green
            "yellow" -> R.color.note_yellow
            "peach" -> R.color.note_peach
            "pink" -> R.color.note_pink
            "lavender" -> R.color.note_lavender
            "white" -> R.color.note_white
            else -> R.color.note_yellow
        }
    }
}