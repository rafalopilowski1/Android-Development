package edu.rafal_opilowski.articlereader.di.viewmodel

import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import edu.rafal_opilowski.articlereader.R
import edu.rafal_opilowski.articlereader.model.repo.FirebaseRssRepository
import edu.rafal_opilowski.articlereader.model.repo.RssRepository

@Module
@InstallIn(ViewModelComponent::class)
interface DiViewModelBindingModule {

    @Binds
    @ViewModelScoped
    fun bindsFirebaseRssRepository(repository: FirebaseRssRepository): RssRepository
}


