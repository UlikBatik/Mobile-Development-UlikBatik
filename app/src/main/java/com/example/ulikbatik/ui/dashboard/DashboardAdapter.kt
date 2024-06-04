package com.example.ulikbatik.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.databinding.ItemPostBinding
import com.example.ulikbatik.ui.detailPost.DetailPostActivity

class DashboardAdapter(private val posts: List<PostModel>) :
    RecyclerView.Adapter<DashboardAdapter.PostViewHolder>() {

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

    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostModel) {
            binding.apply {
                Glide.with(binding.root)
                    .load(post.postImg)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imagePost)

                usernameTv.text = post.userId

                itemPost.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    intent.putExtra(DetailPostActivity.EXTRA_ID_POST, post.postId)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }
}
