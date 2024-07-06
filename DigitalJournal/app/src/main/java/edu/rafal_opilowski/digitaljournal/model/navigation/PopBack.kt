package edu.rafal_opilowski.digitaljournal.model.navigation

import androidx.navigation.NavController

class PopBack:Destination() {
    override fun navigate(navigationController: NavController) {
        navigationController.popBackStack()
    }
}