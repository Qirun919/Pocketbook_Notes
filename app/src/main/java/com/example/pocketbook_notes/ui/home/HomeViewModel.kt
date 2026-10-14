package com.example.pocketbook_notes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.pocketbook_notes.MyApp
import androidx.lifecycle.viewModelScope
import com.example.pocketbook_notes.data.repo.NotesRepo
import com.example.pocketbook_notes.data.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



class HomeViewModel (
    private val repo: NotesRepo
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())

    val notes: StateFlow<List<Note>> = _notes


    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllNotes().collect {items ->

                val active = items
                    .filter { it.deletedAt == 0L }
                    .sortedByDescending { it.updatedAt }

                _notes.update {
                    active
                }
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNote(note)
        }
    }


    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                HomeViewModel(app.repo)
            }
        }
    }
}