package com.example.ulikbatik.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.databinding.ItemScrappingBinding


class DetailCatalogAdapter(private val posts: List<PostModel>) :
    RecyclerView.Adapter<DetailCatalogAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemScrappingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun bind(item: PostModel) {


        }
    }
}
