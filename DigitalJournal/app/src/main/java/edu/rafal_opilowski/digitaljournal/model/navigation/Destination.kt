package edu.rafal_opilowski.digitaljournal.model.navigation

import androidx.navigation.NavController
import java.util.concurrent.atomic.AtomicBoolean

abstract class Destination {
    private val consumed = AtomicBoolean(false)

    abstract fun navigate(navigationController: NavController)

    fun resolve(navigationController: NavController) {
        if (consumed.compareAndSet(false,true)) {
            navigate(navigationController)
        }
    }
}