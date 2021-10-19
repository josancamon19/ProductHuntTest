package com.josancamon19.producthunttest.views.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.apollographql.apollo.coroutines.await
import com.bumptech.glide.Glide
import com.josancamon19.producthunttest.UserDetailsQuery
import com.josancamon19.producthunttest.databinding.FragmentProfileBinding
import com.josancamon19.producthunttest.network.apolloClient

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val args: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        lifecycleScope.launchWhenResumed {
            val response = apolloClient().query(UserDetailsQuery(args.userId)).await()
            response.data?.user?.let { setupViews(it) }
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews(user: UserDetailsQuery.User) {
        Glide.with(binding.root)
            .load(user.coverImage).centerCrop()
            .into(binding.ivProfileBanner)

        Glide.with(binding.root)
            .load(user.profileImage).circleCrop()
            .into(binding.ivProfileImage)

        binding.tvProfileName.text=  user.name
        if (user.headline.isNullOrEmpty()){
            binding.tvProfileHeadline.visibility = View.GONE
        }else binding.tvProfileHeadline.text = user.headline
        binding.tvProfileId.text = "#${user.id} ${user.username} ${user.headline}"
    }
}