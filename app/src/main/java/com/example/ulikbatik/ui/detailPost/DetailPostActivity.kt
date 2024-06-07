package com.example.ulikbatik.ui.detailPost

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.ActivityDetailPostBinding
import com.example.ulikbatik.ui.catalog.detailCatalog.DetailCatalogActivity
import com.example.ulikbatik.ui.factory.PostViewModelFactory

class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    private val detailPostViewModel: DetailPostViewModel by viewModels {
        PostViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setToolbar()
        setView()
    }

    private fun setView() {

        val postId = intent.getStringExtra(EXTRA_ID_POST)

        if (postId != null){
            detailPostViewModel.getPost(postId).observe(this){ res ->
                binding.apply {
                    Glide.with(this@DetailPostActivity)
                        .load(res.data?.postImg)
                        .placeholder(R.drawable.img_placeholder)
                        .into(image)

                    detailUsername.text = res.data?.userId
                    detailDescription.text = res.data?.caption

                    tagName.itemTag.setOnClickListener {
                        val intent = Intent(this@DetailPostActivity, DetailCatalogActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    companion object{
        const val EXTRA_ID_POST = "extra_id_post"
    }
}