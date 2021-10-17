package com.josancamon19.producthunttest.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.adapters.RecyclerPostAdapter
import com.josancamon19.producthunttest.databinding.FragmentHomeBinding
import com.josancamon19.producthunttest.network.apolloClient
import timber.log.Timber

class HomeFragment : Fragment(), RecyclerPostAdapter.OnPostClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postsAdapter: RecyclerPostAdapter

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
        postsAdapter = RecyclerPostAdapter(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = postsAdapter
        lifecycleScope.launchWhenResumed {
            val response = apolloClient().query(HomePostsQuery()).await()
            Timber.d(response.data?.posts?.edges?.toString())
            postsAdapter.submitList(response.data?.posts?.edges)
        }
    }

    override fun setOnParamClick(post: HomePostsQuery.Node) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(post.id)
        Navigation.findNavController(binding.root).navigate(action)
    }
}