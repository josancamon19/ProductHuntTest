package com.josancamon19.producthunttest.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class UserType {
    HUNTER,
    MAKER
}

@Parcelize
data class User(
    val id: String,
    val name: String,
    val headline: String?,
    val profileImage: String?,
    val type: UserType
) : Parcelable
