package edu.rafal_opilowski.articlereader.repo

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import javax.inject.Inject


class FirebaseRssRepository @Inject constructor(
    private val rssParser: RssParser
): RssRepository {
    override suspend fun getChannel(): RssChannel =
        getChannel("https://www.polsatnews.pl/rss/wszystkie.xml")

    override suspend fun getChannel(url: String): RssChannel = rssParser.getRssChannel(url)
}