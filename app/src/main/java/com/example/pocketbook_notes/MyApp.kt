package com.example.pocketbook_notes

import android.app.Application
import com.example.pocketbook_notes.data.repo.NotesRepo
import com.example.pocketbook_notes.data.repo.NotesRepoImpl
import com.google.firebase.firestore.FirebaseFirestore

class MyApp: Application() {
    lateinit var repo: NotesRepo

    override fun onCreate() {
        super.onCreate()

        repo = NotesRepoImpl(
            FirebaseFirestore.getInstance().collection("notes")
        )
    }
}