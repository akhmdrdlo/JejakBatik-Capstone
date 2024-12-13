package com.example.jejak_batik.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.jejak_batik.R
import com.example.jejak_batik.data.model.history.HistoryItem
import com.example.jejak_batik.databinding.FragmentDetailBinding
import com.example.jejak_batik.room.AppDatabase
import com.example.jejak_batik.room.LocalHistoryDao
import com.example.jejak_batik.room.LocalHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    private lateinit var localHistoryDao: LocalHistoryDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        localHistoryDao = AppDatabase.getDatabase(requireContext()).localHistoryDao()
        setupUI(args.historyItem)
        return binding.root
    }

    private fun setupUI(historyItem: HistoryItem) {
        binding.motifNameTextView.text = historyItem.result
        binding.createdAtValueTextView.text = historyItem.createdAt
        binding.motifDescriptionTextView.text = historyItem.data?.description ?: "Deskripsi tidak tersedia"
        binding.occasionDescriptionTextView.text = historyItem.data?.occasion ?: "Tidak ada rekomendasi"

        Glide.with(this)
            .load(historyItem.imageUrl)
            .placeholder(R.drawable.ic_place_holder)
            .into(binding.previewImageView)

        binding.saveLocalButton.setOnClickListener {
            saveToLocal(historyItem)
        }
    }

    private fun saveToLocal(historyItem: HistoryItem) {
        val localHistory = LocalHistoryEntity(
            title = historyItem.result,
            createdAt = historyItem.createdAt,
            description = historyItem.data?.description,
            occasion = historyItem.data?.occasion,
            imageUrl = historyItem.imageUrl
        )

        lifecycleScope.launch(Dispatchers.IO) {
            localHistoryDao.insert(localHistory)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "History disimpan secara lokal!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

