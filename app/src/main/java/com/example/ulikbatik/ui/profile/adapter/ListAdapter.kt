package com.example.ulikbatik.ui.profile.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.remote.response.PostItem
import com.example.ulikbatik.databinding.ItemPostProfileBinding
import com.example.ulikbatik.ui.detailPost.DetailPostActivity
import com.example.ulikbatik.utils.helper.DateLocaleHelper

class ListAdapter(private val posts: List<PostItem>) :
    RecyclerView.Adapter<ListAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemPostProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class PostViewHolder(private val binding: ItemPostProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostItem) {
            binding.apply {
                Glide.with(binding.root)
                    .load(post.pOSTIMG)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imagePost)
                tvBatikName.text = post.batik.bATIKNAME
                tvTotalLikesPost.text = post.lIKES.toString()
                val dateFormatted = DateLocaleHelper.formatDateString(post.cREATEDAT)
                tvDatePost.text = dateFormatted ?: post.cREATEDAT
                itemPost.setOnClickListener {
                    val intent = Intent(binding.root.context, DetailPostActivity::class.java)
                    intent.putExtra(DetailPostActivity.EXTRA_ID_POST, post.pOSTID)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }
}