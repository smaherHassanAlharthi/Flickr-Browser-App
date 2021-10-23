package com.example.flickrbrowserapp


import com.google.gson.annotations.SerializedName

data class Flickr(
    @SerializedName("photos")
    val photos: Photos?,
    @SerializedName("stat")
    val stat: String?
)