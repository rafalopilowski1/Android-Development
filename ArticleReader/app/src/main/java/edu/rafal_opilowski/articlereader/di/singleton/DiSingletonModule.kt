package edu.rafal_opilowski.articlereader.di.singleton

import android.content.Context
import coil.ImageLoader
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.prof18.rssparser.RssParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiSingletonModule {
    @Provides
    @Singleton
    fun providesFacebookLoginManager(): LoginManager =
        LoginManager.getInstance()

    @Provides
    @Singleton
    fun providesFacebookCallbackManager(): CallbackManager = CallbackManager.Factory.create()

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext context: Context
    ): ImageLoader = ImageLoader
        .Builder(context)
        .crossfade(true)
        .build()

    @Provides
    @Singleton
    fun provideRssParser(): RssParser = RssParser()

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseRealtimeDb(): FirebaseDatabase = Firebase.database
}