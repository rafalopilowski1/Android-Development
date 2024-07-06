package edu.rafal_opilowski.articlereader.ui.composable.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _email = mutableStateOf("")
    val email get() = _email

    private val _password = mutableStateOf("")
    val password get() = _password

    private val _uiState = MutableStateFlow<LoginState>(LoginState.LoggedOut)
    val uiState
        get() = _uiState.asStateFlow().onEach {
            if ((_email.value.isEmpty() || _password.value.isEmpty()) && _uiState.value != LoginState.LoggingInViaFacebook) _uiState.update { LoginState.LoggedOut }
            if (auth.currentUser != null) _uiState.update { LoginState.LoggedIn }
        }

    private val _isPasswordVisible = MutableStateFlow(false)
    val isPasswordVisible get() = _isPasswordVisible.asStateFlow()

    fun onLoginButtonClicked() = viewModelScope.launch {
        _uiState.update { LoginState.LoggingIn }
        auth.signInWithEmailAndPassword(email.value, password.value).addOnSuccessListener {
            _uiState.update { LoginState.LoggedIn }
        }.addOnFailureListener {
            _uiState.update { LoginState.LoggedOut }
            when (it) {
                is FirebaseAuthInvalidCredentialsException -> _uiState.update { LoginState.InvalidCredentials }
            }
        }
    }

    fun onEmailValueChanged(login: String) = viewModelScope.launch { _email.value = login }
    fun onPasswordValueChanged(password: String) =
        viewModelScope.launch { _password.value = password }

    fun changePasswordVisibility() = viewModelScope.launch { _isPasswordVisible.update { !it } }
    fun onRegisterButtonClicked() = viewModelScope.launch {
        auth.createUserWithEmailAndPassword(email.value, password.value).addOnSuccessListener {
            _uiState.update { LoginState.LoggedIn }
        }.addOnFailureListener {
            when (it) {
                is FirebaseAuthUserCollisionException -> _uiState.update { LoginState.RegisterEmailAlreadyExists }
                is FirebaseAuthWeakPasswordException -> _uiState.update { LoginState.WeakPassword }
                is FirebaseAuthInvalidCredentialsException -> _uiState.update { LoginState.InvalidEmail }
            }
        }

    }


    fun onFacebookLoginButtonClicked() {
        _uiState.update { LoginState.LoggingIn }
    }

    fun onFacebookLogin(token: String) {
        val credential = FacebookAuthProvider.getCredential(token)
        auth
            .signInWithCredential(credential)
            .addOnSuccessListener {
                _uiState.update { LoginState.LoggedIn }
            }
            .addOnFailureListener {
                _uiState.update { LoginState.LoggedOut }
            }
    }

    fun onFacebookCancel() {
        _uiState.update { LoginState.LoggedOut }
    }
}