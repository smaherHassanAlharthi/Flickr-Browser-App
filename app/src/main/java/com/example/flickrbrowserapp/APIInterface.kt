package com.example.flickrbrowserapp
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET
    fun getPhotos(@Url url: String): Call<Flickr?>?


}
