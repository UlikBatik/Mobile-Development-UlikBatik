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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.databinding.ActivityDetailPostBinding
import com.example.ulikbatik.ui.catalog.DetailCatalogActivity
import com.example.ulikbatik.ui.customView.CustomDialog
import com.example.ulikbatik.ui.factory.PostViewModelFactory
import com.example.ulikbatik.ui.profile.ProfileActivity

class DetailPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPostBinding
    private var userModel: UserModel? = null
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
        detailPostViewModel.apply {
            userModel = user
        }
    }

    private fun setView() {
        val postId = intent.getStringExtra(EXTRA_ID_POST) ?: return
        val userId = userModel?.uSERID

        observeLoading()
        fetchPost(postId, userId)
    }

    private fun observeLoading() {
        detailPostViewModel.isLoadingProduct.observe(this@DetailPostActivity) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun fetchPost(postId: String, userId: String?) {
        detailPostViewModel.getPost(postId).observe(this@DetailPostActivity) { res ->
            if (res.status) {
                res.data?.let { data ->
                    displayPostDetails(data)
                    setupUserInteractions(data, userId)
                    showScrapRelatedProduct(data)
                }
            } else {
                handlePostError(res.message.toInt())
            }
        }
    }

    private fun showScrapRelatedProduct(data: PostModel) {
        detailPostViewModel.getScrapData(data.batik.bATIKNAME)
            .observe(this@DetailPostActivity) { res ->
                if (res.status && res.result != null) {
                    binding.apply {
                        rcScrapping.layoutManager = LinearLayoutManager(
                            this@DetailPostActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        rcScrapping.adapter = DetailPostAdapter(res.result)
                    }
                } else {
                    handlePostError(res.message.toInt())
                }
            }
    }


    private fun displayPostDetails(data: PostModel) {
        binding.apply {
            Glide.with(this@DetailPostActivity)
                .load(data.postImg)
                .placeholder(R.drawable.img_placeholder)
                .into(image)

            detailUsername.text = data.user.uSERNAME
            detailDescription.text = data.caption
            tvTotalLikesPost.text = data.likes.toString()

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
                openDetailCatalogActivity(data.batikId)
            }
        }
    }

    private fun openDetailCatalogActivity(batikId: String) {
        val intent = Intent(this@DetailPostActivity, DetailCatalogActivity::class.java)
        intent.putExtra(DetailCatalogActivity.EXTRA_IDBATIK, batikId)
        startActivity(intent)
    }

    private fun setupUserInteractions(data: PostModel, userId: String?) {
        binding.apply {
            detailImageProfile.setOnClickListener {
                openProfileActivity(data.user.uSERID)
            }

            detailUsername.setOnClickListener {
                openProfileActivity(data.user.uSERID)
            }

            setupLikesFeature(data, userId)
            setupDeleteFeature(data, userId)
        }
    }

    private fun openProfileActivity(userId: String) {
        val intent = Intent(this@DetailPostActivity, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.EXTRA_ID_GUEST, userId)
        startActivity(intent)
    }

    private fun setupLikesFeature(data: PostModel, userId: String?) {
        if (userId != null) {
            observeLikes(userId, data.postId)
            setupLikeButtonClick(userId, data.postId)
        }
    }

    private fun observeLikes(userId: String, postId: String) {
        detailPostViewModel.getLikes(userId, postId).observe(this@DetailPostActivity) { _ ->
            detailPostViewModel.isLiked.observe(this@DetailPostActivity) { isLiked ->
                binding.detailLikesFab.setImageResource(if (isLiked) R.drawable.ic_likes_fill else R.drawable.ic_likes_unfill)
            }
        }
    }

    private fun setupLikeButtonClick(userId: String, postId: String) {
        binding.detailLikesFab.setOnClickListener {
            detailPostViewModel.likePost(userId, postId)
                .observe(this@DetailPostActivity) { response ->
                    Toast.makeText(this@DetailPostActivity, response.message, Toast.LENGTH_SHORT)
                        .show()
                    detailPostViewModel.toggleLikeStatus()
                }
        }
    }

    private fun setupDeleteFeature(data: PostModel, userId: String?) {
        if (userId != null) {
            showDeleteButton(userId, data.user.uSERID)
            binding.detailDeleteFab.setOnClickListener {

                val title = getString(R.string.delete_post)
                val message = getString(R.string.question_delete_post)
                val customDialog = CustomDialog(this)
                customDialog.showDialog(title, message) { userChoice ->
                    if (userChoice) {
                        detailPostViewModel.deletePost(data.postId)
                            .observe(this@DetailPostActivity) { res ->
                                if (res.status) {
                                    showToast(res.message)
                                    finish()
                                } else {
                                    handlePostError(res.message.toInt())
                                }
                            }
                    }
                }
            }
        }
    }

    private fun handlePostError(error: Int) {
        when (error) {
            400 -> showToast(getString(R.string.error_invalid_input))
            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
            503 -> showToast(getString(R.string.error_server_500))
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
