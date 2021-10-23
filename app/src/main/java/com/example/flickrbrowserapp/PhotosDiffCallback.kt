package com.example.flickrbrowserapp

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil

class PhotosDiffCallback (private val oldList:ArrayList<Photo>, private val newList: ArrayList<Photo>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id === newList.get(newItemPosition).id}

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val (_, value, name) = oldList[oldItemPosition]
            val (_, value1, name1) = newList[newItemPosition]
            return name == name1 && value == value1
        }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }

 }