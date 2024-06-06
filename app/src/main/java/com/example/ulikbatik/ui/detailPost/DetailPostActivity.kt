package com.example.ulikbatik.ui.detailPost

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
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
        val postId = intent.getStringExtra(EXTRA_ID_POST) ?: return
        val pref = UserPreferences.getInstance(this.dataStore)

        detailPostViewModel.getPost(postId).observe(this) { res ->
            res.data?.let { data ->
                binding.apply {
                    Glide.with(this@DetailPostActivity)
                        .load(data.postImg)
                        .placeholder(R.drawable.img_placeholder)
                        .into(image)

                    detailUsername.text = data.user.uSERNAME
                    detailDescription.text = data.caption

                    Glide.with(this@DetailPostActivity)
                        .load(data.batik.bATIKIMG)
                        .placeholder(R.drawable.img_placeholder)
                        .into(tagName.imgBatik)

                    tagName.batikName.text = data.batik.bATIKNAME
                    tagName.batikLoc.text = data.batik.bATIKLOCT

                    tagName.itemTag.setOnClickListener {
                        startActivity(Intent(this@DetailPostActivity, DetailCatalogActivity::class.java))
                    }

                    lifecycleScope.launch {
                        pref.getUserId().collect { userId ->
                            if (userId != null) {
                                detailPostViewModel.getLikes(userId, data.postId).observe(this@DetailPostActivity) { likes ->
                                    detailPostViewModel.isLiked.observe(this@DetailPostActivity) { isLiked ->
                                        detailLikesFab.setImageResource(if (isLiked) R.drawable.ic_likes_fill else R.drawable.ic_likes_unfill)
                                    }
                                }
                            }

                            detailLikesFab.setOnClickListener {
                                userId?.let { userIdNotNull ->
                                    detailPostViewModel.likePost(userIdNotNull, data.postId).observe(this@DetailPostActivity) { response ->
                                        Toast.makeText(this@DetailPostActivity, response.message, Toast.LENGTH_SHORT).show()
                                        detailPostViewModel.toggleLikeStatus()
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