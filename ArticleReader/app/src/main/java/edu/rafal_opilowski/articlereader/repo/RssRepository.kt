package edu.rafal_opilowski.articlereader.repo

import com.prof18.rssparser.model.RssChannel

interface RssRepository {
    suspend fun getChannel(): RssChannel;
    suspend fun getChannel(url: String): RssChannel;
}
