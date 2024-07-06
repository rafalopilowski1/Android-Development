package edu.rafal_opilowski.articlereader.ui.composable.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.rafal_opilowski.articlereader.navigation.Destination

@Composable
fun ArticleReaderBottomBar(
    navController: NavController,
    items: List<Destination>,
    isLoggedIn: Boolean,
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val visibleItems =
            if (isLoggedIn) items.filter { it.loggedInOnly }
            else items.filterNot { it.loggedInOnly }
        visibleItems.forEach { screen ->
            NavigationBarItem(selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.drawableId), contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(screen.resourceId))
                })
        }
    }
}