package edu.rafal_opilowski.digitaljournal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rafal_opilowski.digitaljournal.adapter.NoteListAdapter
import edu.rafal_opilowski.digitaljournal.adapter.SwipeToRemove
import edu.rafal_opilowski.digitaljournal.databinding.FragmentListBinding
import edu.rafal_opilowski.digitaljournal.viewmodel.ListViewModel

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    private lateinit var noteListAdapter: NoteListAdapter
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var authorized = false
    private val viewModel: ListViewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(layoutInflater, container, false).also {
            binding = it; binding.viewModel = viewModel; binding.lifecycleOwner =
            viewLifecycleOwner;
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        biometricPrompt =
            BiometricPrompt(this, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showNotes()
                    authorized = true
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    activity?.finish()
                }
            })
        promptInfo = BiometricPrompt
            .PromptInfo
            .Builder()
            .setTitle("Log in to Digital Journal")
            .setAllowedAuthenticators(DEVICE_CREDENTIAL)
            .build()
        if (!authorized)
            biometricPrompt.authenticate(promptInfo)
        else showNotes()
        noteListAdapter = NoteListAdapter(viewModel::onEditNote)

        viewModel.dishes.observe(viewLifecycleOwner) {
            noteListAdapter.noteList = it
        }
        viewModel.navigation.observe(viewLifecycleOwner) {
            it.resolve(findNavController())
        }
    }

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(viewModel::onDestinationChanged)
    }

    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(viewModel::onDestinationChanged)
        super.onStop()
    }

    fun showNotes() {
        val callback = SwipeToRemove(viewModel::onNoteRemove)
        binding.foodList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteListAdapter
            ItemTouchHelper(callback).attachToRecyclerView(this)
        }
    }
}
