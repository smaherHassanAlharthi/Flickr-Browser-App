package com.example.flickrbrowserapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import androidx.recyclerview.widget.LinearSnapHelper

import androidx.recyclerview.widget.SnapHelper




class ListActivity : AppCompatActivity() {

    var photos=ArrayList<Photo>()
    var filteredPhotos=photos
    private lateinit var myRv: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    lateinit var flickr: Flickr
    var imagesNumber=24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pager)

        //get the keyword
        val keyword= intent.getStringExtra("key") //get the keyword

        setBottomNivigation()

        if (keyword!!.isNotEmpty()) {
            createApiInterface(keyword)
        }
        else
            createApiInterface("Cats")//default search

    }


    private fun setBottomNivigation() {
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val menu: Menu = bottomNavigationView.menu
        val menuItem: MenuItem = menu.getItem(1)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainActivity-> startActivity(Intent(this,MainActivity::class.java))
                R.id.listView-> startActivity(Intent(this,ListActivity::class.java))
                R.id.likedActivity-> startActivity(Intent(this,SavedActivity::class.java))
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val menuItem = menu?.findItem(R.id.action_search)
        if(photos!=null){
            if (menuItem != null) {
                val searchItem = menuItem.actionView as SearchView
                searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText!!.isNotEmpty()) {
                            filteredPhotos.clear()
                            val search = newText!!.toLowerCase(Locale.getDefault())
                            photos.forEach {
                                if (it.title?.toLowerCase(Locale.getDefault()).toString()
                                        .contains(search)
                                ) {
                                    filteredPhotos.add(it)
                                }
                            }
                            rvAdapter.updateList(filteredPhotos)
                            myRv.adapter!!.notifyDataSetChanged()
                        } else {
                            filteredPhotos.clear()
                            filteredPhotos.addAll(photos)
                            rvAdapter.updateList(filteredPhotos)
                            myRv.adapter!!.notifyDataSetChanged()
                        }
                        return true
                    }
                })}
        }
        return true
    }

    fun createApiInterface(keyword: String){

        //show progress Dialog
        val progressDialog = ProgressDialog(this)
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
                photos= flickr.photos?.photo!!
                setRV()
                // myRv.scrollToPosition( rvAdapter.getItemCount() - 1)
            }

            override fun onFailure(call: Call<Flickr?>, t: Throwable?) {
                Toast.makeText(applicationContext,"Unable to load data!", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                call.cancel()
            }
        })
    }

    fun setRV() {
        myRv = findViewById(R.id.rvPhotosPager)
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(myRv)
        rvAdapter =RVAdapter(photos, this)
        myRv.adapter = rvAdapter
        myRv.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL, false
            )
        )

    }



}