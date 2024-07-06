package edu.rafal_opilowski.articlereader.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import edu.rafal_opilowski.articlereader.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Destination(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val drawableId: Int,
    val loggedInOnly: Boolean
) {

    data object Articles :
        Destination(
            "/articles",
            R.string.articles,
            R.drawable.baseline_collections_bookmark_24,
            true
        )

    data object Login : Destination("/login", R.string.login, R.drawable.login, false)
}



