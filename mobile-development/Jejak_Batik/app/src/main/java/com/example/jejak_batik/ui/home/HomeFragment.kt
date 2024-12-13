package com.example.jejak_batik.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.FragmentHomeBinding
import com.example.jejak_batik.ui.catalog.CatalogAdapter
import com.example.jejak_batik.ui.catalog.CatalogViewModel
import com.example.jejak_batik.ui.main.MainActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CatalogViewModel by viewModels()
    private lateinit var catalogAdapter: CatalogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scanButton = view.findViewById<Button>(R.id.scanBatikButton)
        scanButton.setOnClickListener {
            findNavController().navigate(R.id.action_currentFragment_to_cameraFragment)

            (activity as? MainActivity)?.binding?.nafBottomNavigation?.show(3)
        }
    }

    private fun setupRecyclerView() {
        catalogAdapter = CatalogAdapter(emptyList()) { selectedItem ->
            val action = HomeFragmentDirections.actionHomeFragmentToDescriptionFragment(
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
            catalogAdapter.updateList(catalogItems.take(4))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
