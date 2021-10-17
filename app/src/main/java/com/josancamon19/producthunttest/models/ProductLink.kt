package com.josancamon19.producthunttest.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductLink(val type: String?, val url: String?) : Parcelable