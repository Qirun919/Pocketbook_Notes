package com.example.pocketbook_notes.data.repo

import com.example.pocketbook_notes.data.model.Note
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NotesRepoImpl(
    private val collectionReference: CollectionReference
) : NotesRepo {

    override fun getAllNotes(): Flow<List<Note>> = callbackFlow {
        val listener = collectionReference.addSnapshotListener { snapshots, exception ->
            if (exception != null) {
                exception.printStackTrace()
                close(exception)
                return@addSnapshotListener
            }

            val notes = mutableListOf<Note>()
            snapshots?.documents?.let { items ->
                for (item in items) {
                    item.data?.let {
                        notes.add(Note.fromHashMap(item.id, it))
                    }
                }
                trySend(notes)
            }
        }

        awaitClose { listener.remove() }
    }

    override suspend fun insertNote(note: Note) {
        collectionReference.add(note.toHashMap()).await()
    }

    override suspend fun getNote(id: String): Note? {
        val snapshot = collectionReference.document(id).get().await()
        return snapshot.data?.let {
            Note.fromHashMap(snapshot.id, it)
        }
    }

    override suspend fun updateNote(id: String, note: Note) {
        collectionReference.document(id).set(note.toHashMap()).await()
    }

    override suspend fun deleteNote(id: String) {
        collectionReference.document(id).delete().await()
    }
}