package com.example.ulikbatik.ui.catalog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ulikbatik.data.Catalog
import com.example.ulikbatik.data.Post
import com.example.ulikbatik.databinding.ItemCatalogBinding
import com.example.ulikbatik.ui.dashboard.DashboardAdapter

class CatalogAdapter(private val posts: List<Catalog>) : RecyclerView.Adapter<CatalogAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(private val binding: ItemCatalogBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Catalog) {
            binding.imgBatik.setImageResource(post.imageRes)
        }
    }

}