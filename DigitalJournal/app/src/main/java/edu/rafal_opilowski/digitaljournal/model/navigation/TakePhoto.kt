package edu.rafal_opilowski.digitaljournal.model.navigation

import androidx.navigation.NavController
import edu.rafal_opilowski.digitaljournal.R

class TakePhoto : Destination() {
    override fun navigate(navigationController: NavController) {
        navigationController.navigate(
            R.id.action_formFragment_to_photoFragment
        )
    }
}