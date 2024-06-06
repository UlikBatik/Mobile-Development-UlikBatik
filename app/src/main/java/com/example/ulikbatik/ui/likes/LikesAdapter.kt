package com.example.ulikbatik.ui.likes

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.databinding.ItemLikesBinding
import com.example.ulikbatik.ui.detailPost.DetailPostActivity

class LikesAdapter(private val posts: List<LikesModel>) : RecyclerView.Adapter<LikesAdapter.PostViewHolder>() {

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

        fun bind(post: LikesModel) {
            binding.apply{
                Glide.with(binding.root)
                    .load(post.post.postImg)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imagePost)

                usernameTv.text = post.post.userId
                descriptionTv.text = post.post.caption

                itemLikes.setOnClickListener{
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    intent.putExtra(DetailPostActivity.EXTRA_ID_POST, post.postId)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

}