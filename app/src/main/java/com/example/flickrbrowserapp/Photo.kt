package com.example.flickrbrowserapp

import com.google.gson.annotations.SerializedName

data class Photo(
    @SerializedName("farm")
    val farm: Int?=null,
    @SerializedName("id")
    val id: String?=null,
    @SerializedName("isfamily")
    val isfamily: Int?=null,
    @SerializedName("isfriend")
    val isfriend: Int?=null,
    @SerializedName("ispublic")
    val ispublic: Int?=null,
    @SerializedName("owner")
    val owner: String?=null,
    @SerializedName("secret")
    val secret: String?=null,
    @SerializedName("server")
    val server: String?=null,
    @SerializedName("title")
    val title: String?=null,
    var checked: Boolean?=null

)