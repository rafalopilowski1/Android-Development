package edu.rafal_opilowski.articlereader.ui.composable.login

sealed class LoginState {
    data object LoggedOut : LoginState()
    data object LoggingIn: LoginState()
    data object RegisterEmailAlreadyExists: LoginState()
    data object InvalidEmail: LoginState()
    data object InvalidCredentials: LoginState()
    data object WeakPassword: LoginState()
    data object LoggedIn : LoginState()
    data object LoggingInViaFacebook: LoginState()
}