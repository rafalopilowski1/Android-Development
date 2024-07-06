package edu.rafal_opilowski.przeterminarz.model

import android.content.Context
import edu.rafal_opilowski.przeterminarz.R

enum class Type : Localizable {
    FOOD, MEDICINE, COSMETICS;

    override fun toLocalizedString(context: Context): String {
        return when (this) {
            FOOD -> context.getString(R.string.food)
            MEDICINE -> context.getString(R.string.medicine)
            COSMETICS -> context.getString(R.string.cosmetic)
        }
    }
}