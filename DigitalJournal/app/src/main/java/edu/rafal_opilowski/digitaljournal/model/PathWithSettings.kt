package edu.rafal_opilowski.digitaljournal.model

import android.graphics.Color
import android.graphics.Path

data class PathWithSettings(
    val path: Path = Path(),
    val color: Int = Color.BLACK,
    val size: Float
)
