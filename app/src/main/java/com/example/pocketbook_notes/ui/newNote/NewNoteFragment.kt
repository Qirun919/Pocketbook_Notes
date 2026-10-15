package com.example.pocketbook_notes.ui.newNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pocketbook_notes.R
import com.example.pocketbook_notes.databinding.FragmentNewNoteBinding

class NewNoteFragment : Fragment() {

    private val viewModel: NewNoteViewModel by viewModels {
        NewNoteViewModel.factory
    }

    private lateinit var binding: FragmentNewNoteBinding

    private var selectedColour = "blue"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupColours()
        updateColourSelection()

        binding.closeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            saveNote()
        }
    }


    // setup color
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

    // update color
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
            else -> R.color.note_blue
        }
    }


    // save notes
    private fun saveNote() {
        val title = binding.titleInput.text.toString().trim()
        val text = binding.bodyInput.text.toString().trim()

        // check if empty or not
        if (title.isEmpty() && text.isEmpty()) {
            findNavController().popBackStack()
            return
        }

        viewModel.saveNote(title, text, selectedColour)

        // save and go back to main
        findNavController().popBackStack()
    }
}