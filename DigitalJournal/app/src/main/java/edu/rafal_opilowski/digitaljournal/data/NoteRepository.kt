package edu.rafal_opilowski.digitaljournal.data

import edu.rafal_opilowski.digitaljournal.model.Note

interface NoteRepository {
    suspend fun getNoteList(): List<Note>
    suspend fun add(note: Note)
    suspend fun getNoteById(id: Int): Note
    suspend fun set(note: Note)
    suspend fun removeById(id: Int)
    suspend fun getNextId(): Int

    companion object {
        const val GENERATE_ID = 0
    }
}