package edu.rafal_opilowski.articlereader.ui.composable.article

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import edu.rafal_opilowski.articlereader.R
import edu.rafal_opilowski.articlereader.ui.theme.Typography


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticleCard(
    guid: String,
    imageUrl: String?,
    title: String,
    description: String,
    link: String,
    viewed: Boolean,
    favourite: Boolean,
    onCardClick: () -> Unit,
    onCardLongClick: () -> Unit,
    onFavouriteClick: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val uri = remember {
        Uri.parse(link)
    }
    val browserIntent = remember {
        CustomTabsIntent.Builder().build()
    }
    if (viewed) {
        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(onLongClick = { onCardLongClick() }) {
                    browserIntent.launchUrl(context, uri); onCardClick()
                },
            colors = CardDefaults.outlinedCardColors(),
            border = CardDefaults.outlinedCardBorder(),
        ) {
            CardContent(
                imageUrl, title, description, uri, favourite, onFavouriteClick
            )
        }
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { browserIntent.launchUrl(context, uri); onCardClick() },
        ) {
            CardContent(
                imageUrl, title, description, uri, favourite, onFavouriteClick
            )
        }
    }
}

@Composable
fun CardContent(
    imageUrl: String?,
    title: String,
    description: String,
    uri: Uri,
    favourite: Boolean,
    onFavouriteClick: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val shareIntent = remember {
        val _intent = Intent(Intent.ACTION_SEND)
        _intent.type = "text/plain"
        _intent.putExtra(Intent.EXTRA_TEXT, uri.toString())
    }
    val shareModalTitle = stringResource(R.string.share_using)

    Column(modifier = Modifier.padding(8.dp)) {
        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp))) {
            imageUrl?.let {
                ArticlePhoto(it)
            }
        }
        Text(
            title, style = Typography.titleMedium
        )
        Text(description, style = Typography.bodyMedium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onFavouriteClick(favourite) })
            {
                val stringId = if (favourite) R.drawable.star else R.drawable.star_border
                Icon(painter = painterResource(id = stringId), contentDescription = null)
            }
            IconButton(onClick = {
                if (shareIntent.resolveActivity(packageManager) != null) {
                    startActivity(
                        context, Intent.createChooser(shareIntent, shareModalTitle), null
                    )
                }
            }) {
                Icon(painter = painterResource(R.drawable.share), contentDescription = null)
            }
        }
    }
}
