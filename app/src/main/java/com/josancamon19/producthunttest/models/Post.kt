package com.josancamon19.producthunttest.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Post(
    val commentsCount: Int?,
    val createdAt: Date?,
    val description: String,
    val featuredAt: Date,
    val id: String,
    val isCollected: Boolean,
    val isVoted: Boolean,
    val makers: List<User?>?,
    val media: List<Media>?,
    val name: String,
    val productLinks: List<ProductLink?>?,
    val reviewsCount: Int?,
    val reviewsRating: Float?,
    val slug: String?,
    val tagline: String?,
    val thumbnail: Media,
    val url: String?,
    val user: User?,
    val userId: Int?,
    val votesCount: Int?,
    val website: String?
) : Parcelable