package com.josancamon19.producthunttest.views.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.PostDetailsQuery
import com.josancamon19.producthunttest.databinding.FragmentDetailBinding
import com.josancamon19.producthunttest.network.apolloClient
import timber.log.Timber

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

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}