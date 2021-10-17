package com.josancamon19.producthunttest.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(val type: String?, val url: String?, val videoUrl: String?): Parcelable
