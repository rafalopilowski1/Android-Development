package edu.rafal_opilowski.articlereader.ui.composable.article

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import edu.rafal_opilowski.articlereader.R

@Composable
fun ArticlePhoto(
    imageUrl: String,
    viewModel: ArticlePhotoViewModel = hiltViewModel()
) {
    AsyncImage(
        model = ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        imageLoader = viewModel.imageLoader,
        placeholder = painterResource(R.drawable.photo),
        error = painterResource(R.drawable.no_photo),
        fallback = painterResource(R.drawable.no_photo),
        modifier = Modifier.aspectRatio(1.78f),
        clipToBounds = true
    )
}