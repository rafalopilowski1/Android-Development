package edu.rafal_opilowski.digitaljournal.data

import android.content.Context
import edu.rafal_opilowski.digitaljournal.data.db.NoteDb
import edu.rafal_opilowski.digitaljournal.model.Note
import edu.rafal_opilowski.digitaljournal.model.db.NoteEntry.Companion.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepositoryInFile(val context: Context) : NoteRepository {
    val db: NoteDb = NoteDb.open(context)
    override suspend fun getNoteList(): List<Note> = withContext(Dispatchers.IO) {
        db.note.getAll().map { it.toNote(context) }
    }

    override suspend fun add(note: Note) = withContext(Dispatchers.IO) {
        db.note.createOrUpdate(note.toEntity())
    }

    override suspend fun getNoteById(id: Int): Note = withContext(Dispatchers.IO) {
        db.note.getById(id.toLong()).toNote(context)
    }

    override suspend fun set(note: Note) = withContext(Dispatchers.IO) {
        db.note.createOrUpdate(note.toEntity())
    }

    override suspend fun removeById(id: Int) = withContext(Dispatchers.IO) {
        db.note.removeById(id.toLong())
    }

    override suspend fun getNextId(): Int = withContext(Dispatchers.IO) {
        db.note.getLatestId() + 1
    }

}