package com.example.ulikbatik.ui.detailPost

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.databinding.ActivityDetailPostBinding
import com.example.ulikbatik.ui.dashboard.PostViewModelFactory
import com.example.ulikbatik.ui.detailCatalog.DetailCatalogActivity
import kotlinx.coroutines.launch

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

        if (postId != null) {
            val pref = UserPreferences.getInstance(this.dataStore)
            detailPostViewModel.getPost(postId).observe(this) { res ->
                binding.apply {
                    Glide.with(this@DetailPostActivity)
                        .load(res.data?.postImg)
                        .placeholder(R.drawable.img_placeholder)
                        .into(image)

                    detailUsername.text = res.data?.user?.uSERNAME
                    detailDescription.text = res.data?.caption

                    Glide.with(this@DetailPostActivity)
                        .load(res.data?.batik?.bATIKIMG)
                        .placeholder(R.drawable.img_placeholder)
                        .into(tagName.imgBatik)

                    tagName.batikName.text = res.data?.batik?.bATIKNAME
                    tagName.batikLoc.text = res.data?.batik?.bATIKLOCT

                    tagName.itemTag.setOnClickListener {
                        val intent =
                            Intent(this@DetailPostActivity, DetailCatalogActivity::class.java)
                        startActivity(intent)
                    }

                    if (res.data != null) {
                        var isLiked: Boolean? = false

                        detailPostViewModel.getLikes(res.data.userId).observe(this@DetailPostActivity) { likes ->
                            isLiked = likes.data?.find { like ->
                              like.postId == res.data.postId
                            } != null

                            detailLikesFab.setImageResource(if (isLiked == true) R.drawable.ic_likes_fill else R.drawable.ic_likes_unfill)
                        }

                        detailLikesFab.setOnClickListener {
                           lifecycleScope.launch {
                                pref.getUserId().collect{ userId ->
                                    val postID = res.data.postId
                                    userId?.let { it1 ->
                                        detailPostViewModel.likePost(it1, postID).observe(this@DetailPostActivity) { response ->
                                            Toast.makeText(
                                                this@DetailPostActivity,
                                                response.message,
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            detailLikesFab.setImageResource(if (isLiked == true) R.drawable.ic_likes_unfill else R.drawable.ic_likes_fill)
                                        }
                                    }
                                }
                           }
                        }
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