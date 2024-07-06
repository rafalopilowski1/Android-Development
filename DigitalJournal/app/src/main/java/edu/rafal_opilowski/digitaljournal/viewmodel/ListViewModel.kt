package edu.rafal_opilowski.digitaljournal.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import edu.rafal_opilowski.digitaljournal.R
import edu.rafal_opilowski.digitaljournal.data.NoteRepository
import edu.rafal_opilowski.digitaljournal.data.RepositoryLocator
import edu.rafal_opilowski.digitaljournal.model.Note
import edu.rafal_opilowski.digitaljournal.model.navigation.AddNote
import edu.rafal_opilowski.digitaljournal.model.navigation.Destination
import edu.rafal_opilowski.digitaljournal.model.navigation.EditNote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
    private var NoteRepository: NoteRepository = RepositoryLocator.noteRepository
    val navigation = MutableLiveData<Destination>()
    val dishes: MutableLiveData<List<Note>> = MutableLiveData(emptyList())

    fun onAddNote() {
        navigation.value = AddNote()
    }

    fun onEditNote(id: Int) {
        navigation.value = EditNote(id)
    }

    fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        println(destination)
        if (destination.id == R.id.listFragment) {
            loadNotes()
        }
    }

    fun onNoteRemove(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            NoteRepository.removeById(id)
            loadNotes()
        }
    }

    private fun loadNotes() {
        viewModelScope.launch(Dispatchers.Main) {
            dishes.value = NoteRepository.getNoteList()
        }
    }
}