package edu.rafal_opilowski.digitaljournal.fragments

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.rafal_opilowski.digitaljournal.R
import edu.rafal_opilowski.digitaljournal.databinding.FragmentRecordingBinding
import edu.rafal_opilowski.digitaljournal.viewmodel.FormViewModel


class RecordingFragment : Fragment() {
    private lateinit var binding: FragmentRecordingBinding
    private val viewModel: FormViewModel by activityViewModels<FormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkPermission(android.Manifest.permission.RECORD_AUDIO)) {
            requestPermission(android.Manifest.permission.RECORD_AUDIO) { onMicrophoneOn() }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recording, container, false)
    }

    private fun onMicrophoneOn() {
        context?.let { ctx ->
            val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(ctx)
            } else {
                MediaRecorder()
            }
            with(mediaRecorder) {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            }
//            viewModel.mediaRecorder.value = mediaRecorder
        }
    }

    fun checkPermission(perm: String): Boolean {
        context?.let { ctx ->
            return ctx.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun requestPermission(perm: String, callback: () -> Unit) {
        context?.let {
            val activityResult = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { perm ->
                if (perm) {
                    callback()
                }
            }
            activityResult.launch(perm)
        }
    }
}