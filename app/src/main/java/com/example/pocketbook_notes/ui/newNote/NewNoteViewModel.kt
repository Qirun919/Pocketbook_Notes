package com.example.pocketbook_notes.ui.newNote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import com.example.pocketbook_notes.MyApp
import com.example.pocketbook_notes.data.model.Note
import com.example.pocketbook_notes.data.repo.NotesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewNoteViewModel(
    private val repo: NotesRepo
) : ViewModel() {

    fun saveNote(title: String, text: String, colour: String) {
        val now = System.currentTimeMillis()
        val note = Note(
            title = title,
            text = text,
            colour = colour,
            createdAt = now,
            updatedAt = now
        )

        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNote(note)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                NewNoteViewModel(app.repo)
            }
        }
    }
}