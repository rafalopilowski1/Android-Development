package edu.rafal_opilowski.przeterminarz.model

import android.content.Context
import edu.rafal_opilowski.przeterminarz.R

enum class Expiration : Localizable {
    USABLE, EXPIRED;

    override fun toLocalizedString(context: Context): String {
        return when (this) {
            EXPIRED -> context.getString(R.string.expired)
            USABLE -> context.getString(R.string.usable)
        }
    }
}