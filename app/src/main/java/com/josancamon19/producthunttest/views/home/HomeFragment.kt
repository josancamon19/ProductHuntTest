package com.josancamon19.producthunttest.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.josancamon19.producthunttest.adapters.RecyclerPostAdapter
import com.josancamon19.producthunttest.databinding.FragmentHomeBinding
import com.josancamon19.producthunttest.models.Media
import com.josancamon19.producthunttest.models.Post
import java.util.*

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
        fakeData()
    }

    private fun fakeData() {
        val fake = mutableListOf<Post>()
        for (i in 0..10) {
            fake.add(
                Post(
                    20,
                    Date(),
                    "DeSo NFT Marketplace",
                    Date(),
                    "$i",
                    true,
                    true,
                    listOf(),
                    listOf(),
                    "Polygram",
                    listOf(),
                    123,
                    4.99f,
                    "Slug",
                    "Tagline",
                    Media(
                        type = "image",
                        "https://ph-files.imgix.net/f3e37964-074f-4d99-bd1f-22f7679391cd.png",
                        null
                    ),
                    "https://www.producthunt.com/posts/polygram-3",
                    null,
                    12,
                    32,
                    "website.com"
                )
            )
        }
        postsAdapter.submitList(fake)
    }

    override fun setOnParamClick(post: Post) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(post)
        Navigation.findNavController(binding.root).navigate(action)
    }
}