package com.example.jejak_batik.ui.description

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.FragmentDescriptionBinding


class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        val args = DescriptionFragmentArgs.fromBundle(requireArguments())
        bindData(args)
        return binding.root
    }

    private fun bindData(args: DescriptionFragmentArgs) {
        binding.motifNameTextView.text = args.name
        binding.motifDescriptionTextView.text = args.description
        binding.occasionDescriptionTextView.text = args.occasion
        binding.historyDescriptionTextView?.text   = args.history

        Glide.with(this)
            .load(args.linkImage)
            .placeholder(R.drawable.ic_place_holder)
            .into(binding.previewImageView)

        binding.seeMoreButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(args.linkShop))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
