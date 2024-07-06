package edu.rafal_opilowski.articlereader.ui.composable.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rafal_opilowski.articlereader.model.Article
import edu.rafal_opilowski.articlereader.model.repo.RssRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ArticleListViewModel
@Inject constructor(
    private val rssRepository: RssRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _items: MutableStateFlow<List<Article>> = MutableStateFlow(listOf())
    val items get() = _items.asStateFlow()

    val favourites get() = rssRepository.getFavouriteArticleFlow()

    val visited get() = rssRepository.getVisitedArticleFlow()

    private val _onlyVisited = MutableStateFlow(false)
    val onlyVisited get() = _onlyVisited.asStateFlow()

    private val _onlyFavourite = MutableStateFlow(false)
    val onlyFavourite get() = _onlyFavourite.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.update { true }
            _items.update { rssRepository.getArticles() }
            _isLoading.update { false }
        }

    }

    fun onOnlyVisitedClicked(visited: Map<String, String>) {
        _onlyVisited.update { !it }
        viewModelScope.launch {
            if (onlyVisited.value) {
                _items.update { state ->
                    state.filter {
                        visited.containsValue(it.guid)
                    }
                }
            } else {
                _items.update { rssRepository.getArticles() }
            }
        }

    }

    fun onOnlyFavouriteClicked(favourites: Map<String, String>) {
        _onlyFavourite.update { !it }
        viewModelScope.launch {
            if (onlyFavourite.value) {
                _items.update { state ->
                    state.filter {
                        favourites.containsValue(it.guid)
                    }
                }
            } else {
                _items.update { rssRepository.getArticles() }
            }
        }

    }


    fun onCardClick(guid: String) {
        rssRepository.markArticleAsVisited(guid)
    }

    fun onFavouriteClicked(guid: String, isFavourite: Boolean, favourites: Map<String, String>) {
        val keys =
            favourites.entries.filter { entry -> entry.value == guid }.map { entry -> entry.key }
        if (!isFavourite) {
            rssRepository.markArticleAsFavourite(guid)
        } else keys.forEach { rssRepository.removeArticleFromFavourites(it) }
    }


    fun onCardLongClick(guid: String, visited: Map<String, String>) {
        visited.entries.filter { entry -> entry.value == guid }.map { entry -> entry.key }
            .forEach { rssRepository.removeArticleFromVisited(it) }
    }
}
