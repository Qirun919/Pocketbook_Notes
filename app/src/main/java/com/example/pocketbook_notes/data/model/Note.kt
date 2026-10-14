package com.example.pocketbook_notes.data.model

data class Note(
    val id: String = "",
    val title: String = "",
    val text: String = "",
    val colour: String = "yellow",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val deletedAt: Long = 0L
) {

    fun toHashMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "text" to text,
            "colour" to colour,
            "createdAt" to createdAt,
            "updatedAt" to updatedAt,
            "deletedAt" to deletedAt
        )
    }

    companion object {
        fun fromHashMap(id: String, map: Map<String, Any>): Note {
            return Note(
                id = id,
                title = map["title"] as? String ?: "",
                text = map["text"] as? String ?: "",
                colour = map["colour"] as? String ?: "yellow",
                createdAt = map["createdAt"] as? Long ?: 0L,
                updatedAt = map["updatedAt"] as? Long ?: 0L,
                deletedAt = map["deletedAt"] as? Long ?: 0L
            )
        }
    }
}