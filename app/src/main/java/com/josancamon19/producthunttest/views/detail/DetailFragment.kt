package com.josancamon19.producthunttest.views.detail

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
import com.josancamon19.producthunttest.PostDetailsQuery
import com.josancamon19.producthunttest.databinding.FragmentDetailBinding
import com.josancamon19.producthunttest.databinding.ListItemUserBinding
import com.josancamon19.producthunttest.models.User
import com.josancamon19.producthunttest.models.UserType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels { Factory(args.postId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        viewLifecycleOwner.lifecycleScope.launch { viewModel.postDetails.collect { setupViews(it) } }
        return binding.root
    }

    private fun setupViews(post: PostDetailsQuery.Post) {
        Glide.with(binding.root)
            .load(post.thumbnail?.url).centerCrop()
            .into(binding.ivDetailsThumbnail)

        binding.tvDetailsTitle.text = post.name
        binding.tvDetailsTagline.text = post.tagline
        binding.tvDetailsDescription.text = post.description

        val data = post.media.filter { it.type == "image" }.map { CarouselItem(it.url) }
        binding.carousel.setData(data)

        binding.btnGetIt.setOnClickListener { }
        binding.btnUpvote.setOnClickListener { }
        binding.btnTweetIt.setOnClickListener { }
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

            item.root.setOnClickListener { _ ->
                val action = DetailFragmentDirections.actionDetailFragmentToProfileFragment(it.id)
                Navigation.findNavController(binding.root).navigate(action)
            }

            binding.containerUsersList.addView(item.root)
        }
    }

}