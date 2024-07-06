package edu.rafal_opilowski.articlereader.ui.composable.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.rafal_opilowski.articlereader.R
import edu.rafal_opilowski.articlereader.navigation.Destination
import edu.rafal_opilowski.articlereader.ui.composable.loading.LoadingScreen

@Composable
fun ArticleList(
    navController: NavController, isLoggedIn: Boolean, vm: ArticleListViewModel = hiltViewModel()
) {
    val isLoading by vm.isLoading.collectAsState()
    if (!isLoggedIn) navController.navigate(Destination.Login.route)
    val items by vm.items.collectAsState()
    val visited by vm.visited.collectAsState(mapOf())
    val favourite by vm.favourites.collectAsState(mapOf())
    val onlyFavourite by vm.onlyFavourite.collectAsState()
    val onlyVisited by vm.onlyVisited.collectAsState()

    if (isLoading) {
        LoadingScreen()
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilterChip(selected = onlyVisited,
                    enabled = !onlyFavourite,
                    onClick = { vm.onOnlyVisitedClicked(visited) },
                    label = { Text(stringResource(R.string.visited)) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.visibility),
                            contentDescription = null
                        )
                    })
                FilterChip(selected = onlyFavourite,
                    enabled = !onlyVisited,
                    onClick = { vm.onOnlyFavouriteClicked(favourite) },
                    label = { Text(stringResource(R.string.favourite)) },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = null
                        )
                    })
            }
            LazyColumn(
                contentPadding = PaddingValues(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(items) {
                    ArticleCard(it.guid,
                        it.imageUrl,
                        it.title,
                        it.description,
                        it.link,
                        visited.containsValue(it.guid),
                        favourite.containsValue(it.guid),
                        onCardClick = { vm.onCardClick(it.guid) },
                        onCardLongClick = { vm.onCardLongClick(it.guid, visited) },
                        onFavouriteClick = { isFavourite ->
                            vm.onFavouriteClicked(
                                it.guid, isFavourite, favourite
                            )
                        })
                }
            }
        }
    }
}

