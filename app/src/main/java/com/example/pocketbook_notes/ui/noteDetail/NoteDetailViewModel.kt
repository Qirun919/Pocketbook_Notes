package com.example.pocketbook_notes.ui.noteDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pocketbook_notes.MyApp
import com.example.pocketbook_notes.data.model.Note
import com.example.pocketbook_notes.data.repo.NotesRepo
import com.example.pocketbook_notes.ui.newNote.NewNoteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteDetailViewModel (
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

    fun moveToTrash(id: String) {
        val note = _note.value ?: return
        val trashedNote = note.copy(deletedAt = System.currentTimeMillis())

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(id, trashedNote)
        }
    }

    fun getCopyText(): String {
        return _note.value?.text ?: ""
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer<NoteDetailViewModel> {
                val app = (this[APPLICATION_KEY] as MyApp)
                NoteDetailViewModel(app.repo)
            }
        }
    }
}