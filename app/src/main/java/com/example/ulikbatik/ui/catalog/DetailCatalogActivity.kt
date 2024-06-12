package com.example.ulikbatik.ui.catalog

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.databinding.ActivityDetailCatalogBinding
import com.example.ulikbatik.ui.detailPost.DetailPostAdapter
import com.example.ulikbatik.ui.factory.CatalogViewModelFactory

class DetailCatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCatalogBinding
    private val catalogViewModel: CatalogViewModel by viewModels {
        CatalogViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        observeLoading()
        setToolbar()
    }

    private fun observeLoading() {
        catalogViewModel.isLoadingProduct.observe(this) {
            showLoading(it)
        }
        catalogViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val idBatik = intent.getStringExtra(EXTRA_IDBATIK)
        if (idBatik != null) {
            catalogViewModel.getDetailCatalog(idBatik).observe(this) {
                if (it.status) {
                    setView(it)
                    showRelatedProduct(it.data)
                } else {
                    handlePostError(it.message.toInt())
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setView(it: GeneralResponse<BatikModel>) {
        if (it.data != null) {
            binding.apply {
                Glide.with(root)
                    .load(it.data.bATIKIMG)
                    .placeholder(R.drawable.img_placeholder)
                    .into(image)
                tvBatikName.text = it.data.bATIKNAME
                tvBatikLocation.text = it.data.bATIKLOCT
                tvDetailBatik.text = it.data.bATIKDESC
                tvHistoryBatik.text = it.data.bATIKHIST
            }
        }
    }

    private fun showRelatedProduct(data: BatikModel?) {
        if (data != null) {
            binding.apply {
                catalogViewModel.getScrapData(data.bATIKNAME)
                    .observe(this@DetailCatalogActivity) { res ->
                        if (res.status && res.result != null) {
                            rcScrapping.layoutManager = LinearLayoutManager(
                                this@DetailCatalogActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            rcScrapping.adapter = DetailPostAdapter(res.result)
                        } else {
                            handlePostError(res.message.toInt())
                        }
                    }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.loadingView.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun handlePostError(error: Int) {
        when (error) {
            400 -> showToast(getString(R.string.error_invalid_input))
            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
            503 -> showToast(getString(R.string.error_server_500))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IDBATIK = "extra_idbatik"
    }
}