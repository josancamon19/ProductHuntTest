package com.josancamon19.producthunttest.views.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.apollographql.apollo.coroutines.await
import com.josancamon19.producthunttest.UserDetailsQuery
import com.josancamon19.producthunttest.UserVotedPostsQuery
import com.josancamon19.producthunttest.UsersFollowingQuery
import com.josancamon19.producthunttest.network.apolloClient
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class ProfileViewModel(private val userId: String) : ViewModel() {

    val userDetails = flow {
        val response = apolloClient().query(UserDetailsQuery(userId)).await()
        response.data?.user?.let { emit(it) }
    }

    val votedPosts = Pager(PagingConfig(pageSize = 20, prefetchDistance = 3)) {
        VotedPostsPagingSource(userId)
    }.flow.cachedIn(viewModelScope)

    val usersFollowing = Pager(PagingConfig(pageSize = 20, prefetchDistance = 3)) {
        UserFollowingPageSource(userId)
    }.flow.cachedIn(viewModelScope)
}

class Factory(private val userId: String) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userId) as T
    }
}

class VotedPostsPagingSource(private val userId: String) :
    PagingSource<String, UserVotedPostsQuery.Edge>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, UserVotedPostsQuery.Edge> {
        return try {
            val response =
                apolloClient().query(UserVotedPostsQuery(id = userId, after = params.key ?: ""))
                    .await()
            val posts = response.data?.user?.votedPosts?.edges ?: listOf()
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

    override fun getRefreshKey(state: PagingState<String, UserVotedPostsQuery.Edge>): String? {
        return state.lastItemOrNull()?.cursor
    }
}


class UserFollowingPageSource(private val userId: String) :
    PagingSource<String, UsersFollowingQuery.Edge>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, UsersFollowingQuery.Edge> {
        return try {
            val response =
                apolloClient().query(UsersFollowingQuery(after = params.key ?: "", id = userId))
                    .await()

            val usersFollowing = response.data?.user?.following?.edges ?: listOf()
            Timber.d("Users following size: ${usersFollowing.size}")
            val nextKey = usersFollowing.last().cursor

            LoadResult.Page(
                data = usersFollowing,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(Exception("Error loading next posts"))
        }
    }

    override fun getRefreshKey(state: PagingState<String, UsersFollowingQuery.Edge>): String? {
        return state.lastItemOrNull()?.cursor
    }
}