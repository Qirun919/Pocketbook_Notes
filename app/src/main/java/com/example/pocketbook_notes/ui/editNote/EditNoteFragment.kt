package com.example.pocketbook_notes.ui.editNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pocketbook_notes.R
import com.example.pocketbook_notes.data.model.Note
import com.example.pocketbook_notes.databinding.FragmentEditNoteBinding
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment() {

    private val viewModel: EditNoteViewModel by viewModels {
        EditNoteViewModel.factory
    }

    private lateinit var binding: FragmentEditNoteBinding

    private var noteId: String = ""
    private var selectedColour = "yellow"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteId = arguments?.getString("noteId") ?: return

        setupColours()
        setupButtons(noteId)
        observeNote()
        viewModel.getNote(noteId)
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
        if (binding.titleInput.text.isNullOrEmpty() && binding.bodyInput.text.isNullOrEmpty()) {
            binding.titleInput.setText(note.title)
            binding.bodyInput.setText(note.text)
        }

        selectedColour = note.colour
        updateColourSelection()
    }

    private fun save() {
        android.util.Log.d("EditNote", "Update clicked, id = $noteId")
        val title = binding.titleInput.text.toString().trim()
        val text = binding.bodyInput.text.toString().trim()

        viewModel.updateNote(noteId, title, text, selectedColour)
        findNavController().popBackStack()
    }


    private fun setupButtons(noteId: String) {
        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.updateButton.setOnClickListener {
            save()
        }

        binding.moveToTrashButton.setOnClickListener {
            viewModel.moveToTrash(noteId)
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private fun setupColours() {
        val colourCards = mapOf(
            "blue" to binding.colourBlue,
            "green" to binding.colourGreen,
            "yellow" to binding.colourYellow,
            "peach" to binding.colourPeach,
            "pink" to binding.colourPink,
            "lavender" to binding.colourLavender,
            "white" to binding.colourWhite
        )

        for ((name, card) in colourCards) {
            card.setOnClickListener {
                selectedColour = name
                updateColourSelection()
            }
        }
    }

    private fun updateColourSelection() {
        val colourCards = listOf(
            "blue" to binding.colourBlue,
            "green" to binding.colourGreen,
            "yellow" to binding.colourYellow,
            "peach" to binding.colourPeach,
            "pink" to binding.colourPink,
            "lavender" to binding.colourLavender,
            "white" to binding.colourWhite
        )

        val selectedWidth = (3 * resources.displayMetrics.density).toInt()

        for ((name, card) in colourCards) {
            card.strokeWidth = if (name == selectedColour) selectedWidth else 0
        }

        binding.root.setBackgroundColor(
            ContextCompat.getColor(requireContext(), colourRes(selectedColour))
        )
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
            else -> R.color.note_green
        }
    }
}