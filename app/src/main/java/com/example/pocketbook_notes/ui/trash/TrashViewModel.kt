package com.example.pocketbook_notes.ui.trash

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

class TrashViewModel(
    private val repo: NotesRepo
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        getTrashNotes()
    }

    fun getTrashNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllNotes().collect { items ->
                // 只要被删除的笔记，最近删除的排前面
                val trash = items
                    .filter { it.deletedAt != 0L }
                    .sortedByDescending { it.deletedAt }

                _notes.update { trash }
            }
        }
    }

    // 恢复：deletedAt 改回 0
    fun restore(note: Note) {
        val restored = note.copy(deletedAt = 0L)

        viewModelScope.launch(Dispatchers.IO) {
            repo.updateNote(note.id, restored)
        }
    }

    // 永久删除
    fun deleteForever(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note.id)
        }
    }

    // 清空回收站
    fun emptyTrash() {
        val all = _notes.value

        viewModelScope.launch(Dispatchers.IO) {
            for (note in all) {
                repo.deleteNote(note.id)
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as MyApp)
                TrashViewModel(app.repo)
            }
        }
    }
}