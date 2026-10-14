package com.example.pocketbook_notes.data.repo

import com.example.pocketbook_notes.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepo {

    fun getAllNotes(): Flow<List<Note>>

    suspend fun insertNote(note: Note)

    suspend fun getNote(id: String): Note?

    suspend fun updateNote(id: String, note: Note)

    suspend fun deleteNote(id: String)

    companion object

}