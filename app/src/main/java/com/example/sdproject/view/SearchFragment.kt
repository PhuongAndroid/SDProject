package com.example.sdproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sdproject.MainActivity
import com.example.sdproject.MainViewModel
import com.example.sdproject.R
import com.example.sdproject.adapter.EmployAdapter
import com.example.sdproject.adapter.EmployEvents
import com.example.sdproject.common.replaceFragment
import com.example.sdproject.databinding.FragmentMainBinding
import com.example.sdproject.model.Employ
import com.example.sdproject.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), EmployEvents {

    private lateinit var binding: FragmentMainBinding
    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: SearchViewModel by viewModels()
    private val adapter by lazy {
        EmployAdapter(viewModel.lstSearch.value, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.allData = parentViewModel.allData
            viewModel.lstSearch.value.addAll(viewModel.allData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNew.isVisible = false
        handleObservers()
        registerEvents()
        binding.rcvEmploy.adapter = adapter
    }

    private fun handleObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lstSearch.collect {
                    adapter.setData(it)
                }
            }
        }
        viewModel.isDeleteSuccess.observe(viewLifecycleOwner) {
            adapter.notifyItemRemoved(it)
        }
    }

    private fun registerEvents() {
        binding.edtFind.doAfterTextChanged {
            val textSearch = binding.edtFind.text.toString()
            viewModel.debounceFullName(textSearch)
        }
    }

    override fun onClick(item: Employ) {
        val detailFragment = DetailFragment.newInstance(item)
        (requireActivity() as MainActivity).replaceFragment(
            parentFragmentManager,
            R.id.fragment_container_view_tag,
            detailFragment
        )
    }

    override fun onEdit(item: Employ) {
        val editFragment = EditFragment.newInstance(item)
        (requireActivity() as MainActivity).replaceFragment(
            parentFragmentManager,
            R.id.fragment_container_view_tag,
            editFragment
        )
    }

    override fun onDelete(item: Employ) {
        viewModel.delete(item)
    }
}