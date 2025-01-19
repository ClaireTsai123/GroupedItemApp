package com.tsai.fetchrewardscodingexcercise.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tsai.fetchrewardscodingexcercise.R
import com.tsai.fetchrewardscodingexcercise.databinding.FragmentItemsBinding

class ItemsFragment : Fragment() {
    private var _binding: FragmentItemsBinding? = null
    private val binding: FragmentItemsBinding get() = _binding!!
    private lateinit var viewModel: ItemViewModel
    private lateinit var groupedItemsAdapter: GroupedItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]
        viewModel.fetchItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemsBinding.inflate(inflater, container, false)
        groupedItemsAdapter = GroupedItemsAdapter()
        binding.itemsRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupedItemsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemListLiveData.observe(viewLifecycleOwner) { dataSet ->
            if (dataSet.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
            } else {
                binding.emptyState.visibility = View.GONE
            }
            groupedItemsAdapter.setInitialDataSet(dataSet)
        }
        viewModel.errorMsgLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(),
                getString(R.string.error_msg),
                Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
