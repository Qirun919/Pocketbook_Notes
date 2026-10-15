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

    private var allNotes = emptyList<Note>()

    private var searchText = ""
    private var selectedColour = ""
    private var newestFirst = true


    init {
        getNotes()
    }

    fun getNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllNotes().collect { items ->
                allNotes = items.filter { it.deletedAt == 0L }
                applyFilter()
            }
        }
    }

    // three filter
    fun setSearch(text: String) {
        searchText = text
        applyFilter()
    }

    fun setColour(colour: String) {
        selectedColour = colour
        applyFilter()
    }

    fun toggleSort(): Boolean {
        newestFirst = !newestFirst
        applyFilter()
        return newestFirst
    }

    // apply filter
    private fun applyFilter() {
        var result = allNotes

        // search
        if (searchText.isNotEmpty()) {
            result = result.filter {
                it.title.contains(searchText, ignoreCase = true) ||
                        it.text.contains(searchText, ignoreCase = true)
            }
        }

        // color
        if (selectedColour.isNotEmpty()) {
            result = result.filter { it.colour == selectedColour }
        }

        // up and down
        result = if (newestFirst) {
            result.sortedByDescending { it.updatedAt }
        } else {
            result.sortedBy { it.updatedAt }
        }

        _notes.update { result }
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