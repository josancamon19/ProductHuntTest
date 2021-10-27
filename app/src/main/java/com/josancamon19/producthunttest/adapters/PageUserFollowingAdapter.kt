package com.josancamon19.producthunttest.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.josancamon19.producthunttest.R
import com.josancamon19.producthunttest.UsersFollowingQuery
import com.josancamon19.producthunttest.databinding.ListItemUserBinding

class DiffUtilUserFollowing : DiffUtil.ItemCallback<UsersFollowingQuery.Edge>() {
    override fun areItemsTheSame(
        oldItem: UsersFollowingQuery.Edge,
        newItem: UsersFollowingQuery.Edge
    ): Boolean {
        return oldItem.node.id == newItem.node.id
    }

    override fun areContentsTheSame(
        oldItem: UsersFollowingQuery.Edge,
        newItem: UsersFollowingQuery.Edge
    ): Boolean {
        return oldItem == newItem
    }
}

class PageUserFollowingAdapter :
    PagingDataAdapter<UsersFollowingQuery.Edge, PageUserFollowingAdapter.UserFollowingViewHolder>(
        DiffUtilUserFollowing()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return UserFollowingViewHolder(ListItemUserBinding.bind(view))
    }

    override fun onBindViewHolder(holder: UserFollowingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    class UserFollowingViewHolder(
        private val itemBinding: ListItemUserBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: UsersFollowingQuery.Edge) {
            val user = data.node

            Glide.with(itemBinding.root)
                .load(user.profileImage).centerCrop()
                .into(itemBinding.ivUserImage)

            itemBinding.tvUserName.text = user.name
            if (user.headline.isNullOrEmpty()) itemBinding.tvUserHeadline.visibility = View.GONE
            else itemBinding.tvUserHeadline.text = user.headline
        }
    }

}
