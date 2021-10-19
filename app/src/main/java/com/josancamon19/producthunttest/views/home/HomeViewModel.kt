package com.josancamon19.producthunttest.views.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.network.apolloClient

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    val flow = Pager(PagingConfig(pageSize = 20, prefetchDistance = 3)) {
        PostsPagingSource()
    }.flow.cachedIn(viewModelScope)

}

class PostsPagingSource : PagingSource<String, HomePostsQuery.Edge>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, HomePostsQuery.Edge> {
        try {
            // Start refresh at page 1 if undefined.
            val response = apolloClient().query(HomePostsQuery(after = params.key ?: "")).await()
            val posts = response.data?.posts?.edges ?: listOf()
            val nextKey = posts.last().cursor

            return LoadResult.Page(
                data = posts,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            return LoadResult.Error(Exception("Error loading next posts"))
        }
    }

    override fun getRefreshKey(state: PagingState<String, HomePostsQuery.Edge>): String? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.lastItemOrNull()?.cursor
    }
}