package edu.rafal_opilowski.digitaljournal.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.rafal_opilowski.digitaljournal.model.db.NoteEntry

@Database(
    entities = [NoteEntry::class],
    version = 1
)
abstract class NoteDb : RoomDatabase() {
    abstract val note: NoteDao

    companion object {
        fun open(context: Context): NoteDb =
            Room
                .databaseBuilder(context, NoteDb::class.java, "note.db")
                .createFromAsset("database/note.db")
                .build()

    }
}