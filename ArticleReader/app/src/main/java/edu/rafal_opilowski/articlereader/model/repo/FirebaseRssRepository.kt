package edu.rafal_opilowski.articlereader.model.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel
import edu.rafal_opilowski.articlereader.model.Article
import edu.rafal_opilowski.articlereader.model.Entry
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject


class FirebaseRssRepository @Inject constructor(
    private val rssParser: RssParser,
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : RssRepository {

    private var rssChannel: RssChannel? = null

    private val userPart: DatabaseReference? =
        auth.currentUser?.let { database.getReference(it.providerData.first().uid) }
    private val visited = userPart?.child("visited")
    private val favourites = userPart?.child("favourites")

    private val visitedArticles: Flow<Map<String,String>> = callbackFlow {
        val visitedListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val visitedList = snapshot.children.mapNotNull {
                    it.key?.let { key ->
                        it.getValue<String>()?.let { value ->

                                key to value

                        }
                    }
                }.toMap()
                if (!trySend(visitedList).isSuccess) close()
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val listener = visited?.addValueEventListener(visitedListener)
        if (listener == null) close()
        awaitClose { listener?.let { visited?.removeEventListener(listener) } }
    }

    private val favouritesArticles: Flow<Map<String,String>> = callbackFlow {
        if (favourites == null) close()
        val favoritesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favouriteList = snapshot.children.mapNotNull {
                    it.key?.let { key ->
                        it.getValue<String>()?.let { value ->
                            key to value
                        }
                    }
                }.toMap()
                if (!trySend(favouriteList).isSuccess) close()
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val listener = favourites?.addValueEventListener(favoritesListener)
        if (listener == null) close()
        awaitClose { listener?.let { favourites?.removeEventListener(it) } }
    }

    override suspend fun getArticles(): List<Article> {
        if (rssChannel == null) {
            rssChannel = getChannel()
        }
        if (auth.currentUser == null) return listOf()

        return rssChannel?.items?.mapNotNull {
            it.guid?.let { guid ->
                it.title?.let { title ->
                    it.description?.let { description ->
                        it.link?.let { link ->
                            Article(
                                guid,
                                title,
                                description,
                                it.image,
                                link,
                            )
                        }
                    }
                }
            }
        }.orEmpty()
    }

    override fun markArticleAsVisited(guid: String): String? {
        val key = visited?.push()?.key
        key?.let {
            val childUpdates = hashMapOf<String, Any>(
                key to guid
            )
            visited?.updateChildren(childUpdates)
        }
        return key
    }

    override fun markArticleAsFavourite(guid: String): String? {
        val key = favourites?.push()?.key
        key?.let {
            val childUpdates = hashMapOf<String, Any>(
                key to guid
            )
            favourites?.updateChildren(childUpdates)
        }
        return key
    }

    override fun getFavouriteArticleFlow(): Flow<Map<String,String>> =
        favouritesArticles

    override fun getVisitedArticleFlow(): Flow<Map<String,String>> =
        visitedArticles

    override fun removeArticleFromFavourites(key: String) {
        favourites?.child(key)?.removeValue()
    }

    override fun removeArticleFromVisited(key: String) {
        visited?.child(key)?.removeValue()
    }

    override suspend fun getChannel(): RssChannel =
        getChannel("https://www.polsatnews.pl/rss/wszystkie.xml")

    override suspend fun getChannel(url: String): RssChannel = rssParser.getRssChannel(url)
}