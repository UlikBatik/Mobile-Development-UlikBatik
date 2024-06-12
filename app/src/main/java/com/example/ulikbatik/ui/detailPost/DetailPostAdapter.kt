package com.example.ulikbatik.ui.detailPost

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.remote.response.ScrapperResponse
import com.example.ulikbatik.databinding.ItemScrappingBinding

class DetailPostAdapter(private val posts: List<ScrapperResponse>) :
    RecyclerView.Adapter<DetailPostAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemScrappingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ItemViewHolder(private val binding: ItemScrappingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScrapperResponse) {
            binding.apply {
                Glide.with(root)
                    .load(item.image)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imageView)

                productName.text = item.title
                price.text = item.price
                itemScrapping.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                    root.context.startActivity(intent)
                }
            }
        }
    }
}
