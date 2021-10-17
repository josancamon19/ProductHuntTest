package com.josancamon19.producthunttest.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(
    val coverImage: String,
    val createdAt: Date,
    val headline: String,
    val id: String?,
    val isFollowing: Boolean?,
    val isMaker: Boolean?,
    val isViewer: Boolean?,
    val name: String?,
    val profileImage: String,
    val twitterUsername: String,
    val url: String?,
    val username: String?,
    val websiteUrl: String
): Parcelable
