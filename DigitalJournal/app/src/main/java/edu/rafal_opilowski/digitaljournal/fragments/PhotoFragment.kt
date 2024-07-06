package edu.rafal_opilowski.digitaljournal.fragments

import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import edu.rafal_opilowski.digitaljournal.databinding.FragmentPhotoBinding
import edu.rafal_opilowski.digitaljournal.viewmodel.FormViewModel

class PhotoFragment : Fragment() {
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private val viewModel by activityViewModels<FormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { photo ->
                onPhotoTaken(
                    photo
                )
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentPhotoBinding.inflate(inflater, container, false).also {
            binding = it
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.paintview.setBackgroundColor(Color.WHITE)
        if (!checkPermission(android.Manifest.permission.CAMERA)) {
            binding.photoFab.isEnabled = false
            requestPermission(android.Manifest.permission.CAMERA) { onCameraOn() }
        }
        with(viewModel) {
            takePhoto = { launchCamera() }
            savePhoto = { hasPhoto -> onPhotoSave(hasPhoto) }
            image_id.value?.let { imageUri = createImageUri(); onPhotoTaken(true) }
            navigation.observe(viewLifecycleOwner) {
                it.resolve(findNavController())
            }
        }
    }

    private fun onCameraOn() {
        binding.photoFab.isEnabled = true
    }

    private fun onPhotoTaken(photo: Boolean) {
        if (photo) loadBitmap()
    }

    private fun loadBitmap() {
        val imageUri = imageUri ?: return
        requireContext().contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        }?.let {
            binding.paintview.background = it
        }
    }

    private fun launchCamera() {
        imageUri = createImage()
        cameraLauncher.launch(imageUri)
    }

    private fun createImage(): Uri? {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "${viewModel.noteId.value}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        val uri = requireContext().contentResolver.insert(imagesUri, ct)
        return uri
    }

    private fun createImageUri(): Uri {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        return imagesUri.buildUpon().appendPath("${viewModel.image_id.value}").build()
    }

    private fun onPhotoSave(hasPhoto: Boolean): Int? {
        imageUri = if (!hasPhoto) createImage() else createImageUri()
        val imageOutputStream =
            imageUri?.let { requireContext().contentResolver.openOutputStream(it) }
        val viewBitmap = binding.paintview.drawToBitmap()
        imageOutputStream?.let { viewBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        imageOutputStream?.close()
        return imageUri?.lastPathSegment?.toInt()
    }

    private fun checkPermission(perm: String): Boolean {
        context?.let { ctx ->
            return ctx.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    private fun requestPermission(perm: String, callback: () -> Unit) {
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