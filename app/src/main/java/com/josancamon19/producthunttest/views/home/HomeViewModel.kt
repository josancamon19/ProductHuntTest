package com.josancamon19.producthunttest.views.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.UsersFollowingQuery
import com.josancamon19.producthunttest.network.apolloClient

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val flow = Pager(PagingConfig(pageSize = 20, prefetchDistance = 3)) {
        PostsPagingSource()
    }.flow.cachedIn(viewModelScope)
}

class PostsPagingSource : PagingSource<String, HomePostsQuery.Edge>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, HomePostsQuery.Edge> {
        return try {
            val response = apolloClient().query(HomePostsQuery(after = params.key ?: "")).await()
            val posts = response.data?.posts?.edges ?: listOf()
            val nextKey = posts.last().cursor

            LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(Exception("Error loading next posts"))
        }
    }

    override fun getRefreshKey(state: PagingState<String, HomePostsQuery.Edge>): String? {
        return state.lastItemOrNull()?.cursor
    }
}