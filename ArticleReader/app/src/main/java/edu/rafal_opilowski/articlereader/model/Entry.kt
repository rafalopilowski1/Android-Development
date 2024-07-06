package edu.rafal_opilowski.articlereader.model

import kotlinx.serialization.Serializable

@Serializable
data class Entry(val firebaseId: String, val articleGuid: String)