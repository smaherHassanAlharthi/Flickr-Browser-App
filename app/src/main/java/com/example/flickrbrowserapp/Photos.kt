package com.example.flickrbrowserapp

import com.google.gson.annotations.SerializedName


data class Photos(
    @SerializedName("page")
    val page: Int?=null,
    @SerializedName("pages")
    val pages: Int?=null,
    @SerializedName("perpage")
    val perpage: Int?=null,
    @SerializedName("photo")
    val photo: ArrayList<Photo>?=null,
    @SerializedName("total")
    val total: Int?=null
)