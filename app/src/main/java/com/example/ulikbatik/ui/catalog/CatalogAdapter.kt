package com.example.ulikbatik.ui.catalog

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.databinding.ItemCatalogBinding
import com.example.ulikbatik.ui.catalog.detailCatalog.DetailCatalogActivity

class CatalogAdapter(private val posts: List<BatikModel>) : RecyclerView.Adapter<CatalogAdapter.PostViewHolder>() {

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

        fun bind(data: BatikModel) {
            binding.apply {
                Glide.with(binding.root)
                    .load(data.bATIKIMG)
                    .into(imgBatik)

                itemCatalog.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailCatalogActivity::class.java)
                    intent.putExtra(DetailCatalogActivity.EXTRA_IDBATIK, data.bATIKID)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

}