package com.example.sdproject.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sdproject.MainActivity
import com.example.sdproject.MainViewModel
import com.example.sdproject.R
import com.example.sdproject.adapter.EmployAdapter
import com.example.sdproject.adapter.EmployEvents
import com.example.sdproject.common.replaceFragment
import com.example.sdproject.databinding.FragmentMainBinding
import com.example.sdproject.model.Employ
import com.example.sdproject.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainFragment : Fragment(), EmployEvents {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val adapter by lazy {
        EmployAdapter(viewModel.allData, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.allData = parentViewModel.allData
        if (savedInstanceState == null)
            viewModel.getData()
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
        handleObservers()
        registerEvents()
        binding.rcvEmploy.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.arrEmploy.collect {
                    if (it.isEmpty())
                        return@collect
                    viewModel.allData.remove(null)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.isDeleteSuccess.observe(viewLifecycleOwner) {
            adapter.notifyItemRemoved(it)
        }
    }

    private fun registerEvents() {
        binding.btnNew.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(
                parentFragmentManager,
                R.id.fragment_container_view_tag,
                NewFragment()
            )
        }

        binding.edtFind.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(
                parentFragmentManager,
                R.id.fragment_container_view_tag,
                SearchFragment()
            )
        }

        binding.rcvEmploy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?

                if (!viewModel.isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.allData.size - 1) {
                        loadMore()
                    }
                }
            }
        })
    }

    private fun loadMore() {
        viewModel.allData.add(null)
        adapter.notifyItemInserted(viewModel.allData.size - 1)
        viewModel.getData()
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