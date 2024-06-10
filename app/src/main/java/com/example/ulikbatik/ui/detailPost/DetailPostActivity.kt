package com.example.ulikbatik.ui.detailPost

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.ulikbatik.databinding.ActivityDetailPostBinding
import com.example.ulikbatik.ui.catalog.DetailCatalogActivity
import com.example.ulikbatik.ui.factory.PostViewModelFactory
import com.example.ulikbatik.ui.profile.ProfileActivity
import kotlinx.coroutines.launch

class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    private lateinit var preferences: UserPreferences
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
        setViewModel()
        setView()
    }

    private fun setViewModel() {
        preferences = detailPostViewModel.pref
    }

    private fun setView() {
        val postId = intent.getStringExtra(EXTRA_ID_POST) ?: return
        val userId = detailPostViewModel.idUserData

        detailPostViewModel.apply {
            isLoading.observe(this@DetailPostActivity) {
                showLoading(it)
            }

            getPost(postId).observe(this@DetailPostActivity) { res ->
                if (res.status) {
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

                            Glide.with(this@DetailPostActivity)
                                .load(data.user.pROFILEIMG)
                                .placeholder(R.drawable.ic_profile)
                                .into(detailImageProfile)

                            tagName.batikName.text = data.batik.bATIKNAME
                            tagName.batikLoc.text = data.batik.bATIKLOCT

                            tagName.itemTag.setOnClickListener {
                                val intent = Intent(
                                    this@DetailPostActivity,
                                    DetailCatalogActivity::class.java
                                )
                                intent.putExtra(
                                    DetailCatalogActivity.EXTRA_IDBATIK,
                                    res.data.batikId
                                )
                                startActivity(intent)
                            }

                            lifecycleScope.launch {
                                preferences.getUser().collect { user ->
                                    user?.let { safeUser ->
                                        safeUser.uSERID.let { userIdNotNull ->
                                            detailPostViewModel.getLikes(userIdNotNull, data.postId)
                                                .observe(this@DetailPostActivity) { _ ->
                                                    detailPostViewModel.isLiked.observe(this@DetailPostActivity) { isLiked ->
                                                        detailLikesFab.setImageResource(if (isLiked) R.drawable.ic_likes_fill else R.drawable.ic_likes_unfill)
                                                    }
                                                }
                                        }
                                    }

                                    detailLikesFab.setOnClickListener {
                                        user?.uSERID?.let { userIdNotNull ->
                                            detailPostViewModel.likePost(userIdNotNull, data.postId)
                                                .observe(this@DetailPostActivity) { response ->
                                                    Toast.makeText(
                                                        this@DetailPostActivity,
                                                        response.message,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    detailPostViewModel.toggleLikeStatus()
                                                }
                                        }
                                    }
                                }
                            }

                            detailImageProfile.setOnClickListener {
                                val intent =
                                    Intent(this@DetailPostActivity, ProfileActivity::class.java)
                                intent.putExtra(ProfileActivity.EXTRA_ID_GUEST, data.userId)
                                startActivity(intent)
                            }

                            detailUsername.setOnClickListener {
                                val intent =
                                    Intent(this@DetailPostActivity, ProfileActivity::class.java)
                                intent.putExtra(ProfileActivity.EXTRA_ID_GUEST, data.userId)
                                startActivity(intent)
                            }

                            if (userId != null) {
                                showDeleteButton(userId, data.user.uSERID)
                                detailPostViewModel.getLikes(userId, data.postId)
                                    .observe(this@DetailPostActivity) { _ ->
                                        detailPostViewModel.isLiked.observe(this@DetailPostActivity) { isLiked ->
                                            detailLikesFab.setImageResource(if (isLiked) R.drawable.ic_likes_fill else R.drawable.ic_likes_unfill)
                                        }
                                    }
                                detailLikesFab.setOnClickListener {
                                    detailPostViewModel.likePost(userId, data.postId)
                                        .observe(this@DetailPostActivity) { res ->
                                            Toast.makeText(
                                                this@DetailPostActivity,
                                                res.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            detailPostViewModel.toggleLikeStatus()
                                        }
                                }
                                detailDeleteFab.setOnClickListener {
                                    detailPostViewModel.deletePost(data.postId)
                                        .observe(this@DetailPostActivity) { res ->
                                            if (res.status) {
                                                showToast(res.message)
                                                finish()
                                            } else {
                                                when (res.message.toInt()) {
                                                    500 -> {
                                                        showToast(getString(R.string.error_server_500))
                                                    }
                                                    503 -> {
                                                        showToast(getString(R.string.error_server_500))
                                                    }
                                                }
                                            }
                                        }
                                }
                            }
                        }
                    }
                }else {
                    handlePostError(res.message.toInt())
                }
            }
        }
    }

    private fun handlePostError(error: Int) {
        when (error) {
            400 -> showToast(getString(R.string.error_invalid_input))
            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
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

    private fun showDeleteButton(userId: String, userIdPost: String) {
        binding.detailDeleteFab.visibility = if (userId == userIdPost) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
        binding.loadingView.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID_POST = "extra_id_post"
    }
}
