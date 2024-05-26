package com.example.ulikbatik.ui.boarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ulikbatik.R

class BoardingPagerAdapter(
    private val context: Context,
) : RecyclerView.Adapter<BoardingPagerAdapter.BoardingViewHolder>() {

    private val images = intArrayOf(
        R.drawable.img_ilustration1,
        R.drawable.img_ilustration2,
        R.drawable.img_ilustration3,
        R.drawable.img_ilustration4,
    )

    class BoardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardingViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.boarding_image_layout, parent, false)
        return BoardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardingViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
}