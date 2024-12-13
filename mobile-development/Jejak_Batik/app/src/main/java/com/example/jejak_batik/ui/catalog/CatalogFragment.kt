package com.example.jejak_batik.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jejak_batik.databinding.FragmentCatalogBinding

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CatalogViewModel by viewModels()
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()
        return binding.root
    }

    private fun setupRecyclerView() {
        catalogAdapter = CatalogAdapter(emptyList()) { selectedItem ->
            val action = CatalogFragmentDirections.actionCatalogFragmentToDescriptionFragment(
                selectedItem.name,
                selectedItem.description,
                selectedItem.link_image,
                selectedItem.link_shop,
                selectedItem.occasion,
                selectedItem.history
            )
            findNavController().navigate(action)
        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = catalogAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.catalogItems.observe(viewLifecycleOwner) { catalogItems ->
            catalogAdapter.updateList(catalogItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
