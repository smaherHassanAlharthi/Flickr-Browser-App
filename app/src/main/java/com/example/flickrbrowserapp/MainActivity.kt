package com.example.flickrbrowserapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrbrowserapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var myRv: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    lateinit var lastList: ArrayList<Photo>
    lateinit var flickr: Flickr
    var keyword=""
    var imagesNumber=24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //hide action bar
        getSupportActionBar()?.hide()

        //initialize shared preference
        PreferenceHelper.init(this)

        //to keep the list of liked photos we have to check if sharedpreference is not empty

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSearch.setOnClickListener {
            hideKeyboard()
            if (binding.etSearch.text.isNotEmpty()) {

                //if the user put the number of images change to the new number
                if(binding.etNumber.text.isNotEmpty()){
                    imagesNumber=binding.etNumber.text.toString().toInt()
                    //change number hint
                    binding.etNumber.hint=imagesNumber.toString()
                }

                //call the api interface to retrieve the data
                createApiInterface(binding.etSearch.text.toString())
                keyword=binding.etSearch.text.toString()
            }
            else
                Toast.makeText(this,"Type something",Toast.LENGTH_SHORT).show()
        }

        setBottomNivigation()

    }

    private fun setBottomNivigation() {
        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val menu: Menu = bottomNavigationView.menu
        val menuItem: MenuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mainActivity-> startActivity(Intent(this,MainActivity::class.java))
                R.id.listView-> { val intent =Intent(this, ListActivity::class.java)
                    intent.putExtra("key",keyword) //send the keyword
                    startActivity(intent)
                }
                R.id.likedActivity-> startActivity(Intent(this,SavedActivity::class.java))
            }
            true
        }

    }


    fun createApiInterface(keyword: String)
    {
        //show progress Dialog
        val progressDialog = ProgressDialog(this@MainActivity)
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
                lastList=flickr.photos?.photo!!
                Log.d("67des","size ${lastList.size}")
                setRV(flickr.photos?.photo!!)
                // myRv.scrollToPosition( rvAdapter.getItemCount() - 1)
            }

            override fun onFailure(call: Call<Flickr?>, t: Throwable?) {
                Toast.makeText(applicationContext,"Unable to load data!", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                call.cancel()
            }
        })
    }

    fun setRV(photos: ArrayList<Photo>) {
        myRv = findViewById(R.id.rvPhotos)
        if(myRv.adapter==null)
        {
            rvAdapter =RVAdapter(photos, this)
            myRv.adapter = rvAdapter
            myRv.layoutManager = GridLayoutManager(applicationContext,2)

        }
        else
        {
            //this for DiffUtil
            rvAdapter.updateList(photos)
        }

    }

    fun hideKeyboard()
    {
        // Hide Keyboard
        val hideKeyboard = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        hideKeyboard?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        PreferenceHelper.setItemList(PreferenceHelper.LAST_PHOTOS_LIST, lastList)
    }

    override fun onResume() {
        super.onResume()

        //check if last search in shared preference if yes set rv
        lastList= PreferenceHelper.getItemList(PreferenceHelper.LAST_PHOTOS_LIST)

        //if not empty set RV
        if(lastList!=null) {
            setRV(lastList)
        }

    }

}