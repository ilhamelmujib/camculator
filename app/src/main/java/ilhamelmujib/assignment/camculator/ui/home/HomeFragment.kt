package ilhamelmujib.assignment.camculator.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.android.material.transition.MaterialSharedAxis
import com.google.mlkit.vision.common.InputImage
import ilhamelmujib.assignment.camculator.R
import ilhamelmujib.assignment.camculator.databinding.FragmentHomeBinding
import ilhamelmujib.assignment.camculator.utils.*
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateWindowInsets(binding.root)
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.run {
            btnCamera.setOnClickListener {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    -> {
                        selectImageRequest.launch(cropImageCameraOptions)
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            }

            btnGallery.setOnClickListener {
                exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                selectImageRequest.launch(cropImageGalleryOptions)
            }
        }

        collectFlow(viewModel.events) { event ->
            when (event) {
                is HomeEvents.ShowPermissionInfo -> {
                    showCameraPermissionInfoDialog()
                }
                is HomeEvents.ShowScanLoading -> {
                    binding.run {
                        cardViewLoading.animate().translationX(0f)
                        btnCamera.isEnabled = false
                        btnGallery.isEnabled = false
                    }
                }
                is HomeEvents.ShowScanEmpty -> {
                    showSnackbarShort(
                        message = getString(R.string.no_text_found),
                        anchor = binding.btnCamera
                    )
                    binding.run {
                        cardViewLoading.animate().translationX(-1000f)
                        btnCamera.isEnabled = true
                        btnGallery.isEnabled = true
                    }
                }
                is HomeEvents.ShowScanError -> {
                    showSnackbarShort(
                        message = event.message ?: getString(R.string.something_went_wrong),
                        anchor = binding.btnCamera
                    )
                    binding.run {
                        cardViewLoading.animate().translationX(-1000f)
                        btnCamera.isEnabled = true
                        btnGallery.isEnabled = true
                    }
                }
                is HomeEvents.ShowScanSuccess -> {
                    binding.run {
                        cardViewLoading.animate().translationX(-1000f)
                        btnCamera.isEnabled = true
                        btnGallery.isEnabled = true
                    }
                    val action = HomeFragmentDirections.actionHomeFragmentToScanFragment(event.scan)
                    findNavController().navigate(action)
                }
            }
        }

    }


    private val selectImageRequest = registerForActivityResult(CropImageContract()) {
        if (it.isSuccessful) {
            it.uriContent?.let { handleImage(it) }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                // Permission granted, continue workflow
                selectImageRequest.launch(cropImageCameraOptions)
            } else {
                // Provide explanation on why the permission is needed
                viewModel.handlePermissionDenied()
            }
        }

    private val cropImageGalleryOptions = CropImageContractOptions(
        null,
        CropImageOptions().apply {
            imageSourceIncludeCamera = false
            imageSourceIncludeGallery = true
        })

    private val cropImageCameraOptions = CropImageContractOptions(
        null,
        CropImageOptions().apply {
            imageSourceIncludeCamera = true
            imageSourceIncludeGallery = false
        })

    private fun handleImage(uri: Uri) {
        val image = InputImage.fromFilePath(requireContext(), uri)
        viewModel.handleScan(image)
    }


}