package edu.rafal_opilowski.articlereader.ui.composable.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.rafal_opilowski.articlereader.R
import edu.rafal_opilowski.articlereader.navigation.Destination


@Composable
fun LoginView(
    navController: NavController, vm: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val email by vm.email
    val password by vm.password
    val isPasswordVisible by vm.isPasswordVisible.collectAsState()
    val uiState by vm.uiState.collectAsState(LoginState.LoggedOut)
    when (uiState) {
        is LoginState.LoggedIn -> navController.navigate(Destination.Articles.route)
        else -> {}
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = email,
                onValueChange = { vm.onEmailValueChanged(it) },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = (listOf(
                    LoginState.InvalidCredentials,
                    LoginState.InvalidEmail,
                    LoginState.RegisterEmailAlreadyExists
                ).contains(uiState)),
                supportingText = {
                    val stringId = when (uiState) {
                        LoginState.InvalidCredentials -> R.string.invalid_credentials
                        LoginState.InvalidEmail -> R.string.invalid_email
                        LoginState.RegisterEmailAlreadyExists -> R.string.email_already_exists
                        else -> null
                    }
                    stringId?.let { Text(text = stringResource(it)) }
                })
            OutlinedTextField(value = password,
                onValueChange = { vm.onPasswordValueChanged(it) },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val imageId =
                        if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off
                    IconButton(onClick = { vm.changePasswordVisibility() }) {
                        Icon(painter = painterResource(imageId), contentDescription = null)
                    }
                },
                isError = (listOf(LoginState.InvalidCredentials, LoginState.WeakPassword).contains(
                    uiState
                )),
                supportingText = {
                    val stringId = when (uiState) {
                        LoginState.InvalidCredentials -> R.string.invalid_credentials
                        LoginState.WeakPassword -> R.string.weak_password
                        else -> null
                    }
                    stringId?.let { Text(text = stringResource(it)) }
                })
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = { vm.onLoginButtonClicked() },
                    enabled = ((uiState != LoginState.LoggingIn) && email.isNotEmpty() && password.isNotEmpty()),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.login))
                    }
                }
                Button(
                    onClick = { vm.onRegisterButtonClicked() }, enabled = (!(listOf(
                        LoginState.RegisterEmailAlreadyExists, LoginState.LoggingIn
                    ).contains(uiState)) && email.isNotEmpty() && password.isNotEmpty())
                ) {
                    Text(text = stringResource(R.string.register))
                }
            }
            SignInWithFacebookButton(onFacebookSuccess = { token: String -> vm.onFacebookLogin(token) },
                onFacebookCancel = { vm.onFacebookCancel() },
                onFacebookError = { error ->
                    Toast.makeText(
                        context, error.localizedMessage, Toast.LENGTH_SHORT
                    ).show()
                }) {
                vm.onFacebookLoginButtonClicked()
            }
            if (uiState == LoginState.LoggingIn) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLoginView() {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val isLoggingIn by rememberSaveable { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            OutlinedTextField(value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val imageId =
                        if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off
                    IconButton(onClick = {/* TODO */ }) {
                        Icon(painter = painterResource(imageId), contentDescription = null)
                    }
                })
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(onClick = { /* TODO */ }, enabled = !isLoggingIn) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Log in")
                    }
                }
                Button(onClick = {/* TODO */ }) {
                    Text(text = "Register")
                }
            }
            SignInWithFacebookButton(onFacebookSuccess = {},
                onFacebookCancel = {},
                onFacebookError = {}) {}
            if (isLoggingIn) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}