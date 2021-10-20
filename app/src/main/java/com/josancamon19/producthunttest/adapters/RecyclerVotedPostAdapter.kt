package com.josancamon19.producthunttest.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.R
import com.josancamon19.producthunttest.UserVotedPostsQuery
import com.josancamon19.producthunttest.databinding.ListItemPostBinding

class DiffUtilVotedPost : DiffUtil.ItemCallback<UserVotedPostsQuery.Edge>() {
    override fun areItemsTheSame(
        oldItem: UserVotedPostsQuery.Edge,
        newItem: UserVotedPostsQuery.Edge
    ): Boolean {
        return oldItem.node.id == newItem.node.id
    }

    override fun areContentsTheSame(
        oldItem: UserVotedPostsQuery.Edge,
        newItem: UserVotedPostsQuery.Edge
    ): Boolean {
        return oldItem == newItem
    }
}

class PagedVotedPostsAdapter(private val onPostClick: OnPostClick) :
    PagingDataAdapter<UserVotedPostsQuery.Edge, PagedVotedPostsAdapter.PostViewHolder>(
        DiffUtilVotedPost()
    ) {

    interface OnPostClick {
        fun setOnParamClick(post: UserVotedPostsQuery.Node)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_post, parent, false)
        return PostViewHolder(ListItemPostBinding.bind(view), onPostClick)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    class PostViewHolder(
        private val itemBinding: ListItemPostBinding,
        private val onPostClick: OnPostClick
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: UserVotedPostsQuery.Edge) {
            val post = data.node
            itemBinding.tvTitle.text = post.name
            itemBinding.tvTagline.text = post.tagline
            itemBinding.tvHunter.text = "@${post.user.username}"
            itemBinding.tvVoteCount.text = "${post.votesCount}"

            Glide.with(itemBinding.root)
                .load(post.thumbnail?.url).centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(itemBinding.ivThumbnail)

            itemBinding.root.setOnClickListener {
                onPostClick.setOnParamClick(post)
            }
        }
    }

}
