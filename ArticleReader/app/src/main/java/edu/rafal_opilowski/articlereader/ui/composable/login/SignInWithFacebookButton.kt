package edu.rafal_opilowski.articlereader.ui.composable.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton

@Composable
fun SignInWithFacebookButton(
    vm: SignInWithFacebookButtonViewModel = hiltViewModel(),
    onFacebookSuccess: (String) -> Unit,
    onFacebookError: (FacebookException) -> Unit,
    onFacebookCancel: () -> Unit,
    onClick: () -> Unit
) {
    AndroidView(
        factory = {
            LoginButton(it).apply {
                setPermissions("email", "public_profile")
                registerCallback(vm.callbackManager, object: FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        onFacebookCancel()
                    }

                    override fun onError(error: FacebookException) {
                        onFacebookError(error)
                    }

                    override fun onSuccess(result: LoginResult) {
                        onFacebookSuccess(result.accessToken.token)
                    }

                })
            }
        },
        update = {
            it.setOnClickListener { onClick() }
        }
    )
}

@Preview
@Composable
fun SignInWithFacebookButton(
) {
    AndroidView(
        factory = {
            LoginButton(it).apply {

            }
        }
    )
}