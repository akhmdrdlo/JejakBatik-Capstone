package com.example.jejak_batik.ui.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.data.api.ModelApiConfig
import com.example.jejak_batik.data.pref.ModelRepository
import com.example.jejak_batik.databinding.FragmentCameraBinding
import com.example.jejak_batik.di.ViewModelFactory
import com.example.jejak_batik.helper.getImageUri
import com.example.jejak_batik.helper.reduceFileImage
import com.example.jejak_batik.helper.uriToFile
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null
    private lateinit var modelRepository: ModelRepository

    private val viewModel: CameraViewModel by activityViewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        modelRepository = ModelRepository(ModelApiConfig.getApiService())
        viewModel.fetchEmail()
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.cameraButton.setOnClickListener { captureFromCamera() }
        binding.galleryButton.setOnClickListener { startPhotoPicker() }
        binding.scanButton.setOnClickListener {
            if (croppedImageUri == null) {
                showToast("Pilih atau ambil gambar terlebih dahulu.")
                return@setOnClickListener
            }

            binding.progressIndicator.visibility = View.VISIBLE
            binding.dimBackground.visibility = View.VISIBLE

            lifecycleScope.launch {
                processImage()
            }
        }
    }

    private fun processWithAPI(file: File) {
        viewModel.email.observe(viewLifecycleOwner) { email ->
            modelRepository.predictImage(email, file) { response, error ->
                binding.progressIndicator.visibility = View.GONE
                binding.dimBackground.visibility = View.GONE

                if (response != null) {
                    if (response.status) {
                        val name = response.data?.name ?: "Unknown"
                        val description = response.data?.description ?: "No description available"

                        viewModel.setMotifDetails(name, description)

                        findNavController().navigate(R.id.action_navigation_camera_to_analysis_result)
                    } else {
                        val apiMessage = response.message ?: "Gagal memproses gambar."
                        showToast("Gagal memproses gambar karena $apiMessage")
                    }
                } else {
                    showToast("Gagal memproses gambar dengan API: ${error ?: "Unknown error"}")
                }
            }
        }
    }



    private suspend fun processImage() {
        withContext(Dispatchers.IO) {
            try {
                delay(2000)
                val croppedFile = uriToFile(croppedImageUri!!, requireContext())
                val reducedFile = croppedFile.reduceFileImage()
                if (reducedFile.length() > 1_000_000) {
                    withContext(Dispatchers.Main) {
                        binding.progressIndicator.visibility = View.GONE
                        binding.dimBackground.visibility = View.GONE
                        showToast("Gambar masih terlalu besar meskipun sudah dikompresi.")
                    }
                    return@withContext
                }
                withContext(Dispatchers.Main) {
                    processWithAPI(reducedFile)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressIndicator.visibility = View.GONE
                    binding.dimBackground.visibility = View.GONE
                    showToast("Terjadi kesalahan saat memproses gambar: ${e.message}")
                }
            }
        }
    }

    private fun captureFromCamera() {
        val photoUri = getImageUri(requireContext())
        currentImageUri = photoUri
        launcherIntentCamera.launch(photoUri)
    }

    private fun startPhotoPicker() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            try {
                Glide.with(this).load(uri).preload()
                startUCrop(uri)
            } catch (e: Exception) {
                showToast("Gagal memuat gambar dari galeri: ${e.message}")
            }
        } else {
            showToast("Tidak ada media yang dipilih.")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentImageUri != null) {
            startUCrop(currentImageUri!!)
        } else {
            showToast("Gagal mengambil gambar.")
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))
        try {
            UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(1080, 1080)
                .start(requireContext(), this)
        } catch (e: Exception) {
            showToast("Kesalahan saat memulai UCrop: ${e.message}")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                croppedImageUri = resultUri
                try {
                    Glide.with(this).load(croppedImageUri).into(binding.previewImageView)
                    viewModel.setImageUri(croppedImageUri)
                } catch (e: Exception) {
                    showToast("Gagal memuat hasil gambar: ${e.message}")
                }
            } else {
                showToast("URI hasil cropping kosong.")
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast("Kesalahan saat memotong gambar: ${cropError?.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
