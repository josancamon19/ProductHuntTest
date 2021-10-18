package com.josancamon19.producthunttest.views.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.PostDetailsQuery
import com.josancamon19.producthunttest.UserDetailsQuery
import com.josancamon19.producthunttest.databinding.FragmentDetailBinding
import com.josancamon19.producthunttest.databinding.FragmentProfileBinding
import com.josancamon19.producthunttest.network.apolloClient
import com.josancamon19.producthunttest.views.detail.DetailFragmentArgs
import timber.log.Timber

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
        }
        return binding.root
    }
}