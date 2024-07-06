package edu.rafal_opilowski.articlereader.ui.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.facebook.login.LoginManager
import edu.rafal_opilowski.articlereader.R
import edu.rafal_opilowski.articlereader.navigation.Destination
import edu.rafal_opilowski.articlereader.ui.composable.article.ArticleList
import edu.rafal_opilowski.articlereader.ui.composable.login.LoginView
import edu.rafal_opilowski.articlereader.ui.composable.navigation.ArticleReaderBottomBar
import edu.rafal_opilowski.articlereader.ui.theme.ArticleReaderTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

val items = listOf(
    Destination.Login, Destination.Articles
)

@Composable
fun ArticleReaderApp(
    navController: NavHostController,
    vm: ArticleReaderAppViewModel = hiltViewModel()
) {
    val isLoggedIn by vm.isLoggedIn.collectAsState()
    val scope = rememberCoroutineScope()
    ArticleReaderTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
//            ArticleReaderBottomBar(navController, items, isLoggedIn)
        }, floatingActionButton = {
            if (isLoggedIn) {
                FloatingActionButton(onClick = {
                    scope.launch {
                        vm.logOut()
                    }
                }) {
                    Icon(painter = painterResource(R.drawable.logout), contentDescription = null)
                }
            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Destination.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Destination.Login.route) {
                    LoginView(navController)
                }
                composable(Destination.Articles.route) {
                    ArticleList(navController, isLoggedIn)
                }
            }
        }
    }

}