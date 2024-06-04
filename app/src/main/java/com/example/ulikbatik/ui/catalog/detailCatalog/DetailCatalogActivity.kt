package com.example.ulikbatik.ui.catalog.detailCatalog

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.ActivityDetailCatalogBinding
import com.example.ulikbatik.ui.catalog.CatalogViewModel
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
                if (it.data != null) {
                    binding.apply {
                        Glide.with(root)
                            .load(it.data.bATIKIMG)
                            .placeholder(R.drawable.img_placeholder)
                            .into(image)
                        tvBatikName.text = it.data.bATIKNAME
                        tvBatikLocation.text = it.data.bATIKLOCT
                        tvDetailBatik.text = it.data.bATIKDESC
                    }
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
    }

    companion object {
        const val EXTRA_IDBATIK = "extra_idbatik"
    }
}