package edu.rafal_opilowski.digitaljournal.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import edu.rafal_opilowski.digitaljournal.R
import edu.rafal_opilowski.digitaljournal.databinding.FragmentFormBinding
import edu.rafal_opilowski.digitaljournal.model.FormType
import edu.rafal_opilowski.digitaljournal.viewmodel.FormViewModel


private const val TYPE_KEY = "type"


class FormFragment : Fragment() {
    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private lateinit var locationManager: LocationManager
    private val viewModel by activityViewModels<FormViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(TYPE_KEY, FormType::class.java)
            } else {
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentFormBinding.inflate(layoutInflater, container, false)
            .also {
                binding = it; binding.viewModel = viewModel; binding.lifecycleOwner =
                viewLifecycleOwner;
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationPerms = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (!checkPermissions(locationPerms)) {
            binding.locationButton.isEnabled = false
            requestPermissions(locationPerms) { onLocationOn() }
        } else {
            onLocationOn()
        }

        with(viewModel) {
            init(
                (type as? FormType.Edit)?.id
            )
            currentLocation = { onCurrentLocation() }
            navigation.observe(viewLifecycleOwner) {
                it.resolve(findNavController())
            }
        }
    }

    private fun onLocationOn() {
        binding.locationButton.isEnabled = true
        context?.let {
            locationManager = it.getSystemService(LocationManager::class.java)
        }
    }

    @SuppressLint("MissingPermission")
    fun onCurrentLocation(): Address? {
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return location?.let { getFromLocationAddress(it) }
    }

    private fun getFromLocationAddress(location: Location): Address? {
        requireContext().let {
            val geocoder = Geocoder(it)
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            return addresses?.first()
        }
    }

    private fun checkPermissions(perms: Array<String>): Boolean {
        requireContext().let { ctx ->
            return perms.map { ctx.checkSelfPermission(it) }
                .all { it == PackageManager.PERMISSION_GRANTED }
        }
    }

    private fun requestPermissions(perms: Array<String>, callback: () -> Unit) {
        requireContext().let {
            val activityResult = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { perms ->
                if (perms[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                    callback()
                }
            }
            activityResult.launch(perms)
        }
    }
}