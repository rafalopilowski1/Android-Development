package edu.rafal_opilowski.digitaljournal.model.db

import android.content.Context
import android.location.Geocoder
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.rafal_opilowski.digitaljournal.model.Note

@Entity(tableName = "note")
data class NoteEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    // GPS coords
    val latitude: Double?,
    val longitude: Double?,
    // Image ID
    @ColumnInfo(name = "has_image")
    val image_id: Int?,
    // Recording ID
    @ColumnInfo(name = "has_recording")
    val recording_id: Int?,
) {
    fun toNote(context: Context): Note {
        val geocoder = Geocoder(context)
        val cities = latitude?.let { lat ->
            longitude?.let { long ->
                geocoder.getFromLocation(
                    lat,
                    long,
                    1
                )
            }
        }
        val city = cities?.first()
        return Note(
            id,
            title,
            description,
            city,
            image_id,
            recording_id
        )
    }

    companion object {
        fun Note.toEntity(): NoteEntry {
            return NoteEntry(
                id,
                title,
                description,
                city?.latitude,
                city?.longitude,
                image_id,
                recording_id
            )
        }
    }
}