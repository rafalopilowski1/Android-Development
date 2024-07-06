package edu.rafal_opilowski.przeterminarz.model

import android.content.Context

interface Localizable {
    fun toLocalizedString(context: Context): String
}