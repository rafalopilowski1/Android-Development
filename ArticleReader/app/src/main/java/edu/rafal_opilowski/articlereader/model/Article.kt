package edu.rafal_opilowski.articlereader.model

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val guid: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val link: String,
    val visited: Boolean = false,
    val favourite: Boolean = false
)
