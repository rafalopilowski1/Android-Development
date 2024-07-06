package edu.rafal_opilowski.articlereader.model.repo

import com.prof18.rssparser.model.RssChannel
import edu.rafal_opilowski.articlereader.model.Article
import kotlinx.coroutines.flow.Flow

interface RssRepository {
    suspend fun getArticles(): List<Article>
    suspend fun getChannel(): RssChannel
    suspend fun getChannel(url: String): RssChannel

    fun markArticleAsVisited(guid: String): String?
    fun markArticleAsFavourite(guid: String): String?
    fun getFavouriteArticleFlow(): Flow<Map<String, String>>
    fun getVisitedArticleFlow(): Flow<Map<String, String>>
    fun removeArticleFromFavourites(key: String)
    fun removeArticleFromVisited(key: String)
}
