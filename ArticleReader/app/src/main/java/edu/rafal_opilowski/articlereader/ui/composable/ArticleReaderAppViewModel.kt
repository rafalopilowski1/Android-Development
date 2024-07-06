package edu.rafal_opilowski.articlereader.ui.composable

import androidx.lifecycle.ViewModel
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArticleReaderAppViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val loginManager: LoginManager
) : ViewModel() {
    private var _authStateListener: AuthStateListener

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn get() = _isLoggedIn.asStateFlow()

    init {
        _authStateListener = AuthStateListener {
            if (it.currentUser != null) _isLoggedIn.update { true }
        }
        auth.addAuthStateListener(_authStateListener)
    }

    override fun onCleared() {
        auth.removeAuthStateListener(_authStateListener)
    }

    fun logOut() {
        auth.signOut()
        loginManager.logOut()
        _isLoggedIn.update { false }
    }
}
