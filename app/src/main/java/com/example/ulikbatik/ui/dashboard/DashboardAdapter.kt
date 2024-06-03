package com.example.ulikbatik.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ulikbatik.data.Post
import com.example.ulikbatik.data.PostDummy
import com.example.ulikbatik.databinding.ItemPostBinding
import com.example.ulikbatik.ui.detailPost.DetailPostActivity

class DashboardAdapter(private val posts: List<Post>) : RecyclerView.Adapter<DashboardAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply{

                imagePost.setImageResource(post.imageRes)
                usernameTv.text = post.username

                itemPost.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }
}