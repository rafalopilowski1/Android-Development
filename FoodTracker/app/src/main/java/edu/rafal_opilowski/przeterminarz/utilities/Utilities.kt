package edu.rafal_opilowski.przeterminarz.utilities

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showSnackbar(view: View, prompt: String) {
    Snackbar.make(
        view, prompt, Snackbar.LENGTH_LONG
    ).show()
}
