package edu.rafal_opilowski.articlereader.ui.composable.article

import androidx.lifecycle.ViewModel
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlePhotoViewModel @Inject constructor(
    val imageLoader: ImageLoader
) : ViewModel()
