package edu.rafal_opilowski.articlereader.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.credentials.CredentialManager
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import dagger.hilt.android.AndroidEntryPoint
import edu.rafal_opilowski.articlereader.ui.composable.ArticleReaderApp
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ArticleReaderApp(
                navController = navController,
            )
        }
    }
}