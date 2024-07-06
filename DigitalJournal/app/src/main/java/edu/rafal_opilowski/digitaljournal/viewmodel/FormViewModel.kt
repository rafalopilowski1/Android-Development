package edu.rafal_opilowski.digitaljournal.viewmodel

import android.location.Address
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import edu.rafal_opilowski.digitaljournal.R
import edu.rafal_opilowski.digitaljournal.data.NoteRepository
import edu.rafal_opilowski.digitaljournal.data.RepositoryLocator
import edu.rafal_opilowski.digitaljournal.model.Note
import edu.rafal_opilowski.digitaljournal.model.navigation.Destination
import edu.rafal_opilowski.digitaljournal.model.navigation.MakeRecording
import edu.rafal_opilowski.digitaljournal.model.navigation.PopBack
import edu.rafal_opilowski.digitaljournal.model.navigation.TakePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FormViewModel : ViewModel() {
    private var repository: NoteRepository = RepositoryLocator.noteRepository
    var edited = MutableLiveData<Note?>(null)

    lateinit var currentLocation: () -> Address?
    lateinit var takePhoto: () -> Unit
    lateinit var savePhoto: (Boolean) -> Int?

    val noteId = MutableLiveData(-1)
    val name = MutableLiveData("")
    val description = MutableLiveData("")
    private val city = MutableLiveData<Address?>()
    val cityText = city.map { it?.locality }
    val image_id = MutableLiveData<Int?>()
    val recording_id = MutableLiveData<Int?>()
    val buttonText = MutableLiveData<Int>()
    val navigation = MutableLiveData<Destination>()

    fun init(
        id: Int?,
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (id == null) noteId.value = repository.getNextId()
            }
        }
        id?.let { id ->
            noteId.value = id
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    edited.value = repository.getNoteById(id).also {
                        name.value = it.title
                        description.value = it.description
                        city.value = it.city
                        if (image_id.value == null)
                            image_id.value = it.image_id
                        if (recording_id.value == null)
                            recording_id.value = it.recording_id
                    }
                }
            }
        }
        buttonText.value = when (edited.value) {
            null -> R.string.add
            else -> R.string.save
        }
    }

    fun onSave() {
        val title = name.value.orEmpty()
        val description = description.value ?: ""
        val city = city.value
        val imageId = image_id.value
        val recordingId = recording_id.value
        val toSave = edited.value?.copy(
            title = title,
            description = description,
            city = city,
            image_id = imageId,
            recording_id = recordingId
        ) ?: Note(
            id = noteId.value!!,
            title = title,
            description = description,
            city,
            imageId,
            recordingId
        )
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (edited.value == null) {
                    repository.add(toSave)
                } else {
                    repository.set(toSave)
                }
            }
        }
        navigation.value = PopBack()
    }


    fun onLocationClicked() {
        city.value = currentLocation()
    }

    fun onRecordingClick() {
        navigation.value = MakeRecording()
    }

    fun onPhotoClick() {
        navigation.value = TakePhoto()
    }

    fun onTakePhoto() {
        takePhoto()
    }

    fun onSavePhoto() {
        image_id.value = savePhoto(image_id.value != null)
        navigation.value = PopBack()
    }
}
