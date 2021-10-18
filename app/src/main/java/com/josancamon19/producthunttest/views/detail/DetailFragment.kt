package com.josancamon19.producthunttest.views.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.apollographql.apollo.coroutines.await
import com.bumptech.glide.Glide
import com.josancamon19.producthunttest.PostDetailsQuery
import com.josancamon19.producthunttest.databinding.FragmentDetailBinding
import com.josancamon19.producthunttest.databinding.ListItemUserBinding
import com.josancamon19.producthunttest.models.User
import com.josancamon19.producthunttest.models.UserType
import com.josancamon19.producthunttest.network.apolloClient

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        lifecycleScope.launchWhenResumed {
            val response = apolloClient().query(PostDetailsQuery(args.postId)).await()
            response.data?.post?.let { setupViews(it) }
        }
        return binding.root
    }

    private fun setupViews(post: PostDetailsQuery.Post) {
        Glide.with(binding.root)
            .load(post.thumbnail?.url).centerCrop()
            .into(binding.ivDetailsThumbnail)

        binding.tvDetailsTitle.text = post.name
        binding.tvDetailsTagline.text = post.tagline
        binding.tvDetailsDescription.text = post.description

        Glide.with(binding.root)
            .load(post.media.firstOrNull()?.url).fitCenter()
            .into(binding.ivDetailsMedia)

        binding.btnGetIt.setOnClickListener { }
        binding.btnUpvote.setOnClickListener { }
        binding.btnTweetIt.setOnClickListener { }
//        binding.tvFeaturedAt.text = post
        setupUsers(post)
    }

    private fun setupUsers(post: PostDetailsQuery.Post) {
        val users = mutableListOf(
            User(
                post.user.id, post.user.name, post.user.headline ?: "Hunter",
                post.user.profileImage, UserType.HUNTER
            )
        )

        post.makers.filter { it.id != post.user.id }.forEach {
            users.add(User(it.id, it.name, it.headline, it.profileImage, UserType.MAKER))
        }

        users.forEach {
            val item =
                ListItemUserBinding.inflate(layoutInflater, binding.containerUsersList, false)

            Glide.with(binding.root).load(it.profileImage).circleCrop().into(item.ivUserImage)
            item.tvUserName.text = it.name
            item.tvUserHeadline.text = it.headline
            item.tvUserType.text = it.type.toString()

            item.root.setOnClickListener {

            }

            binding.containerUsersList.addView(item.root)
        }
    }

}