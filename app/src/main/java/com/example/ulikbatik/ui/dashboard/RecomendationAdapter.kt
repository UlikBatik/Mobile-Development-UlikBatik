package com.example.ulikbatik.ui.detailPost

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.ScrapperResponse
import com.example.ulikbatik.databinding.ItemRecommendationBinding
import com.example.ulikbatik.databinding.ItemScrappingBinding

class RecomendationAdapter(private val posts: List<PostModel>) :
    RecyclerView.Adapter<RecomendationAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class ItemViewHolder(private val binding: ItemRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostModel) {
            binding.apply {
                Glide.with(root)
                    .load(post.postImg)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imageView)

                Glide.with(root)
                    .load(post.user.pROFILEIMG)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imageIcon)

                itemRecomendation.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    intent.putExtra(DetailPostActivity.EXTRA_ID_POST, post.postId)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }
}
