package com.example.pocketbook_notes.ui.adapter

import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketbook_notes.R
import com.example.pocketbook_notes.data.model.Note
import com.example.pocketbook_notes.databinding.ItemTrashBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TrashAdapter(
    private val onRestore: (Note) -> Unit,
    private val onDelete: (Note) -> Unit
) : RecyclerView.Adapter<TrashAdapter.TrashViewHolder>() {

    private var notes = emptyList<Note>()

    fun submitList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    inner class TrashViewHolder(
        private val binding: ItemTrashBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            val context = binding.root.context

            binding.trashTitle.text = note.title.ifEmpty { "Untitled" }
            binding.trashDeletedDate.text = formatDeleted(note.deletedAt)

            binding.colourDot.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, colourRes(note.colour))
            )

            binding.restoreButton.setOnClickListener { onRestore(note) }
            binding.deleteForeverButton.setOnClickListener { onDelete(note) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrashViewHolder {

        val binding = ItemTrashBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TrashViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TrashViewHolder,
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

    private fun formatDeleted(millis: Long): String {
        if (DateUtils.isToday(millis)) return "Deleted today"
        val date = SimpleDateFormat("MMM d", Locale.ENGLISH).format(Date(millis))
        return "Deleted $date"
    }
}