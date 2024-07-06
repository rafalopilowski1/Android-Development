package edu.rafal_opilowski.przeterminarz.model


import java.io.Serializable

sealed class FormType : Serializable {
    data object New : FormType() {
        private fun readResolve(): Any = New
    }

    data class Edit(val item: Item) : FormType()
}