package com.josancamon19.producthunttest.views.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.josancamon19.producthunttest.UserDetailsQuery
import com.josancamon19.producthunttest.UserVotedPostsQuery
import com.josancamon19.producthunttest.adapters.PagedVotedPostsAdapter
import com.josancamon19.producthunttest.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), PagedVotedPostsAdapter.OnPostClick {
    private lateinit var binding: FragmentProfileBinding
    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var postsAdapter: PagedVotedPostsAdapter

    private val viewModel: ProfileViewModel by viewModels { Factory(args.userId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupVotedPostsRecycler()
        setupViewModel()
        return binding.root
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userDetails.collect { setupUserData(it) }
            viewModel.flow.collectLatest { postsAdapter.submitData(it) }
        }
    }

    private fun setupVotedPostsRecycler() {
        postsAdapter = PagedVotedPostsAdapter(this)
        binding.recyclerVotedPosts.setHasFixedSize(true)
        binding.recyclerVotedPosts.adapter = postsAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setupUserData(user: UserDetailsQuery.User) {
        Glide.with(binding.root)
            .load(user.coverImage).centerCrop()
            .into(binding.ivProfileBanner)

        Glide.with(binding.root)
            .load(user.profileImage).circleCrop()
            .into(binding.ivProfileImage)

        binding.tvProfileName.text = user.name
        if (user.headline.isNullOrEmpty()) binding.tvProfileHeadline.visibility = View.GONE
        else binding.tvProfileHeadline.text = user.headline
        binding.tvProfileId.text = "#${user.id} ${user.username} ${user.headline}"
    }

    override fun setOnParamClick(post: UserVotedPostsQuery.Node) {
        val action = ProfileFragmentDirections.actionProfileFragmentToDetailFragment(post.id)
        Navigation.findNavController(binding.root).navigate(action)
    }
}