package com.example.jejak_batik.ui.camera

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.FragmentAnalysisResultBinding

class AnalysisResultFragment : Fragment() {

    private var _binding: FragmentAnalysisResultBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null

    private val viewModel: CameraViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisResultBinding.inflate(inflater, container, false)

        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_save)
                    .skipMemoryCache(true)
                    .into(binding.previewImageView)
            } else {
                showToast("Gambar tidak tersedia.")
            }
        }

        viewModel.motifName.observe(viewLifecycleOwner) { name ->
            if (!name.isNullOrEmpty()) {
                binding.motifNameTextView.text = name
                println("DEBUG: motifName -> $name")
            } else {
                binding.motifNameTextView.text = "Unknown"
            }
        }

        viewModel.description.observe(viewLifecycleOwner) { desc ->
            if (!desc.isNullOrEmpty()) {
                binding.motifDescriptionTextView.text = desc
                println("DEBUG: description -> $desc")
            } else {
                binding.motifDescriptionTextView.text = "No description available"
            }
        }

        binding.scanAgainButton.setOnClickListener {
            croppedImageUri = null
            currentImageUri = null
            viewModel.setImageUri(null)
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
