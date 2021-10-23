package com.example.flickrbrowserapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //hide action bar
        getSupportActionBar()?.hide()


        val link=intent.getStringExtra("photo_link")
        val imageView=findViewById<ImageView>(R.id.imageLarge)
        Glide.with(this)
            .load(Uri.parse(link.toString()))
            .into(imageView)
        val btClose=findViewById<ImageButton>(R.id.btClose)
        btClose.setOnClickListener{
            startActivity(Intent(this@DetailActivity,MainActivity::class.java))
        }
    }
}