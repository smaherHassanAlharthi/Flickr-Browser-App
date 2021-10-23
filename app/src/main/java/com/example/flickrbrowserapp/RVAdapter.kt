package com.example.flickrbrowserapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrbrowserapp.databinding.ItemRowBinding


class RVAdapter(private val photos: ArrayList<Photo>, val context: Context): RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)
    val liked=ArrayList<Photo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val title = photos[position].title
        val farm =  photos[position].farm
        val id = photos[position].id
        val owner = photos?.get(position)?.owner
        val secret = photos[position].secret
        val server = photos[position].server

        if(photos[position].checked==true) {
            holder.binding.imgLike.isChecked = true
        }

        //check if in favorite list check
        if(Constants.liked.contains(photos[position])) {
            holder.binding.imgLike.isChecked = true
            photos[position].checked==true
        }

        holder.binding.apply {
            tvTitle.text=title
            Glide.with(context)
               .load(Uri.parse("https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_q.jpg"))
               .into(imgView)

        }
        holder.itemView.setOnClickListener{
            val intent =Intent(context,DetailActivity::class.java)
            intent.putExtra("photo_link","https://farm${farm}.staticflickr.com/${server}/${id}_${secret}_n.jpg")
            context.startActivity(intent)
        }
        holder.binding.imgLike.setOnClickListener{
            //if checked add it to the checked list
          if(holder.binding.imgLike.isChecked){
              photos[position].checked=true
              holder.binding.imgLike.isChecked=true
              Constants.liked.add(photos[position])
          PreferenceHelper.setItemList(PreferenceHelper.PHOTOS_LIST,  Constants.liked)
          }
        else {
              photos[position].checked=false
              holder.binding.imgLike.isChecked=false
              Constants.liked.remove(photos[position])
              PreferenceHelper.setItemList(PreferenceHelper.PHOTOS_LIST,  Constants.liked)
          }

         }
        holder.binding.tvTitle.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flickr.com/photos/${owner}/${id} "))
            context.startActivity(browserIntent)
        }

    }

    override fun getItemCount() = photos.size


    //implementing DiffUtil in RecyclerView
    fun updateList(newPhotos: ArrayList<Photo>){
        val diffCallback = PhotosDiffCallback(photos, newPhotos)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        photos.clear()
        photos.addAll(newPhotos)
        diffResult.dispatchUpdatesTo(this)
    }


}