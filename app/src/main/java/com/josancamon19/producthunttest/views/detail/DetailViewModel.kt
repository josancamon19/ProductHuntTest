package com.josancamon19.producthunttest.views.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.PostDetailsQuery
import com.josancamon19.producthunttest.network.apolloClient
import kotlinx.coroutines.flow.flow

class DetailViewModel(private val postId: String) : ViewModel() {

    val postDetails = flow {
        val response = apolloClient().query(PostDetailsQuery(postId)).await()
        response.data?.post?.let { emit(it) }
    }
}

class Factory(private val postId: String) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(postId) as T
    }
}