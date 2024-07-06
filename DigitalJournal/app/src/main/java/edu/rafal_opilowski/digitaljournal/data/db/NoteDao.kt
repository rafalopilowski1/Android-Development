package edu.rafal_opilowski.digitaljournal.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.rafal_opilowski.digitaljournal.model.db.NoteEntry

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createOrUpdate(food: NoteEntry)

    @Query("SELECT * FROM note;")
    suspend fun getAll(): List<NoteEntry>

    @Query("SELECT * FROM note WHERE id = :id;")
    suspend fun getById(id: Long): NoteEntry

    @Query("DELETE FROM note WHERE id = :id;")
    suspend fun removeById(id: Long)

    @Query("SELECT id from note ORDER BY id DESC LIMIT 1")
    suspend fun getLatestId(): Int
}