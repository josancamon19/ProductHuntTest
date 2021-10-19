package com.josancamon19.producthunttest.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.adapters.PagedPostsAdapter
import com.josancamon19.producthunttest.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), PagedPostsAdapter.OnPostClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postsAdapter: PagedPostsAdapter

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupRecycler()
        return binding.root
    }

    private fun setupRecycler() {
        postsAdapter = PagedPostsAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = postsAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                postsAdapter.submitData(pagingData)
            }
        }
    }

    override fun setOnParamClick(post: HomePostsQuery.Node) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(post.id)
        Navigation.findNavController(binding.root).navigate(action)
    }
}