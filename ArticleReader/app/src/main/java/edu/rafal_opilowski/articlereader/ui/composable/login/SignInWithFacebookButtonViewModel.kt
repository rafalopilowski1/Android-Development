package edu.rafal_opilowski.articlereader.ui.composable.login

import androidx.lifecycle.ViewModel
import com.facebook.CallbackManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInWithFacebookButtonViewModel @Inject constructor(
    val callbackManager: CallbackManager
):ViewModel() {

}
