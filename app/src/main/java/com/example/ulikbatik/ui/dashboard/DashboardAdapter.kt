package com.example.ulikbatik.ui.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.databinding.ItemPostBinding
import com.example.ulikbatik.ui.detailPost.DetailPostActivity

class DashboardAdapter :
    PagingDataAdapter<PostModel, DashboardAdapter.PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val posts = getItem(position)
        if (posts != null) {
            holder.bind(posts)
        }
    }

    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostModel) {
            binding.apply {
                Glide.with(root)
                    .load(post.postImg)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imagePost)

                Glide.with(root)
                    .load(post.user.pROFILEIMG)
                    .placeholder(R.drawable.ic_profile)
                    .into(imageIcon)

                usernameTv.text = post.user.uSERNAME

                itemPost.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    intent.putExtra(DetailPostActivity.EXTRA_ID_POST, post.postId)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(
                oldItem: PostModel,
                newItem: PostModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PostModel,
                newItem: PostModel
            ): Boolean {
                return oldItem.postId == newItem.postId
            }
        }
    }
}
