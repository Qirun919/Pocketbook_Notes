package com.example.pocketbook_notes.ui.editNote

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val repo: NotesRepo
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note

    fun getNote(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = repo.getNote(id)
            _note.update { item }
        }
    }

    fun updateNote(id: String, title: String, text: String, colour: String) {
        val old = _note.value ?: return

        val updated = old.copy(
            title = title,
            text = text,
            colour = colour,
            updatedAt = System.currentTimeMillis()
        )

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(id, updated)
        }
    }

    fun moveToTrash(id: String) {
        val old = _note.value ?: return
        val trashed = old.copy(deletedAt = System.currentTimeMillis())

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(id, trashed)
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                EditNoteViewModel(app.repo)
            }
        }
    }
}