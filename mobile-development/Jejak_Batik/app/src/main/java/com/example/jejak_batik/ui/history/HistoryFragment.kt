package com.example.jejak_batik.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jejak_batik.databinding.FragmentHistoryBinding
import com.example.jejak_batik.di.ViewModelFactory
import com.example.jejak_batik.ui.main.MainActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(mutableListOf()) { historyItem ->
            val action = HistoryFragmentDirections.actionHistoryFragmentToDetailFragment(historyItem)
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.historyList.observe(viewLifecycleOwner) { historyList ->
            binding.loadingIndicator.visibility = View.GONE

            if (historyList.isNullOrEmpty()) {
                showEmptyStateMessage("History kosong. Anda belum memiliki riwayat pindai.")
            } else {
                hideEmptyStateMessage()
                historyAdapter.updateData(historyList)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showRefreshDialog()
            }
        }

        viewModel.getEmail().observe(viewLifecycleOwner) { email ->
            if (!email.isNullOrEmpty()) {
                binding.loadingIndicator.visibility = View.VISIBLE
                viewModel.fetchHistories(email)
            } else {
                showEmptyStateMessage("Email tidak tersedia.")
            }
        }
    }


    private fun showRefreshDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("History Kosong")
            .setMessage("Silahkan Scan terlebih dahulu")
            .setPositiveButton("Ya") { _, _ ->
                val action = HistoryFragmentDirections.actionHistoryFragmentToNavigationCamera()
                findNavController().navigate(action)
                (activity as? MainActivity)?.binding?.nafBottomNavigation?.show(3)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun showEmptyStateMessage(message: String) {
        binding.emptyState.visibility = View.VISIBLE
        binding.emptyState.text = message
        binding.recyclerView.visibility = View.GONE
    }

    private fun hideEmptyStateMessage() {
        binding.emptyState.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
