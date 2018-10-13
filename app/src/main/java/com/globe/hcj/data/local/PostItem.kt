package com.globe.hcj.data.local

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostItem(
        @SerializedName("title") val title: String = "",
        @SerializedName("star") val star: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("open") val open: String = "",
        @SerializedName("price") val price: String = "",
        @SerializedName("phone") val phone: String = ""
) : Serializable

data class AreaItem(
        @SerializedName("area") val area: String = "",
        @SerializedName("list") val list: ArrayList<PostItem>? = null
) : Serializable