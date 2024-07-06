package edu.rafal_opilowski.digitaljournal.model.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import edu.rafal_opilowski.digitaljournal.R
import edu.rafal_opilowski.digitaljournal.model.FormType

class EditNote(val id: Int) : Destination() {
    override fun navigate(navigationController: NavController) {
        navigationController.navigate(
            R.id.action_listFragment_to_formFragment,
            bundleOf("type" to FormType.Edit(id))
        )
    }
}