package edu.rafal_opilowski.digitaljournal

import android.app.Application
import edu.rafal_opilowski.digitaljournal.data.RepositoryLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RepositoryLocator.init(applicationContext)
    }
}