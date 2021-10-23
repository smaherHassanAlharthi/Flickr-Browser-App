package com.example.flickrbrowserapp

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    private var retrofit: Retrofit? = null
    var flickr:Flickr? = null

    fun getClient(): Retrofit? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/services/rest/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit
    }


    fun createApiInterface(keyword: String, context: Context, imagesNumber:Int): Flickr?
    {
        //show progress Dialog
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient.getClient()?.create(APIInterface::class.java)

        /*tags= A comma-delimited list of tags. Photos with one or more of the
        tags listed will be returned.*/

        val call: Call<Flickr?>? = apiInterface!!.getPhotos(
            "?method=${Constants.METHOD_SEARCH}&api_key=${Constants.API_KEY}" +
                    "&tags=${keyword}&per_page=${imagesNumber}&safe_search=1&format=json&nojsoncallback=1")


        call?.enqueue(object : Callback<Flickr?> {
            override fun onResponse(
                call: Call<Flickr?>?,
                response: Response<Flickr?>
            ) {
                progressDialog.dismiss()

                flickr= response.body()!!

            }

            override fun onFailure(call: Call<Flickr?>, t: Throwable?) {
                Toast.makeText(context,"Unable to load data!", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                call.cancel()
            }
        })
        return flickr
    }
}