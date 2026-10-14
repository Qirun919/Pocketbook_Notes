package com.example.pocketbook_notes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketbook_notes.R
import com.example.pocketbook_notes.data.model.Note
import com.example.pocketbook_notes.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private val onClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = emptyList<Note>()

    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            val context = binding.root.context

            binding.noteTitle.text = note.title.ifEmpty { "Untitled" }
            binding.notePreview.text = note.text
            binding.noteDate.text = formatDate(note.updatedAt)

            binding.noteCard.setCardBackgroundColor(
                ContextCompat.getColor(context, colourRes(note.colour))
            )

            binding.root.setOnClickListener { onClick(note) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteViewHolder {

        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NoteViewHolder,
        position: Int
    ) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
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

    private fun formatDate(millis: Long): String {
        if (millis == 0L) return ""
        return SimpleDateFormat("MMM d", Locale.ENGLISH).format(Date(millis))
    }
}