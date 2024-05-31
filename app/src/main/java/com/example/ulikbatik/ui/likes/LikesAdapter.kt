package com.example.ulikbatik.ui.likes

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ulikbatik.data.Likes
import com.example.ulikbatik.databinding.ItemLikesBinding
import com.example.ulikbatik.ui.detailPost.DetailPostActivity

class LikesAdapter(private val posts: List<Likes>) : RecyclerView.Adapter<LikesAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemLikesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(private val binding: ItemLikesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Likes) {
            binding.apply{
                imagePost.setImageResource(post.imageRes)
                usernameTv.text = post.username
                descriptionTv.text = post.description

                itemLikes.setOnClickListener{
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

}