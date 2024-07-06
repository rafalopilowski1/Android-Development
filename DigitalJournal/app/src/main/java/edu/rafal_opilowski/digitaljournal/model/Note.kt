package edu.rafal_opilowski.digitaljournal.model

import android.location.Address

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val city: Address?,
    val image_id: Int?,
    val recording_id: Int?,
)
