package com.example.agro_town.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.agro_town.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun lodUserPicture(image: Any, imageView: ImageView) {
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}