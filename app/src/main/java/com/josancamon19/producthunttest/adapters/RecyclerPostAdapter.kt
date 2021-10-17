package com.josancamon19.producthunttest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josancamon19.producthunttest.HomePostsQuery
import com.josancamon19.producthunttest.R
import com.josancamon19.producthunttest.databinding.ListItemPostBinding

class DiffUtilPost : DiffUtil.ItemCallback<HomePostsQuery.Edge>() {
    override fun areItemsTheSame(
        oldItem: HomePostsQuery.Edge,
        newItem: HomePostsQuery.Edge
    ): Boolean {
        return oldItem.node.id == newItem.node.id
    }

    override fun areContentsTheSame(
        oldItem: HomePostsQuery.Edge,
        newItem: HomePostsQuery.Edge
    ): Boolean {
        return oldItem == newItem
    }
}

class RecyclerPostAdapter(private val onPostClick: OnPostClick) :
    ListAdapter<HomePostsQuery.Edge, RecyclerPostAdapter.ParameterViewHolder>(DiffUtilPost()) {

    interface OnPostClick {
        fun setOnParamClick(post: HomePostsQuery.Node)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParameterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_post, parent, false)
        return ParameterViewHolder(ListItemPostBinding.bind(view), onPostClick)
    }

    override fun onBindViewHolder(holder: ParameterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ParameterViewHolder(
        private val itemBinding: ListItemPostBinding, private val onPostClick: OnPostClick
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(data: HomePostsQuery.Edge) {
            val post = data.node
            itemBinding.tvTitle.text = post.name
            itemBinding.tvDescription.text = post.description
            itemBinding.tvHunter.text = post.user.username
            itemBinding.tvVoteCount.text = "${post.votesCount}"

            Glide.with(itemBinding.root)
                .load(post.thumbnail?.url).centerCrop()
                .into(itemBinding.ivThumbnail)

            itemBinding.root.setOnClickListener {
                onPostClick.setOnParamClick(post)
            }
        }
    }

}