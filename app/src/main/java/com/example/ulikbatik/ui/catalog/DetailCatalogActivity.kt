package com.example.ulikbatik.ui.catalog

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.ActivityDetailCatalogBinding
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

        catalogViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val idBatik = intent.getStringExtra(EXTRA_IDBATIK)
        if (idBatik != null) {
            catalogViewModel.getDetailCatalog(idBatik).observe(this) {
                if (it.status && it.data != null) {
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
                } else {
                    handlePostError(it.message.toInt())
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.loadingView.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun handlePostError(error: Int){
        when (error) {
            400 -> showToast(getString(R.string.error_invalid_input))
            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IDBATIK = "extra_idbatik"
    }
}