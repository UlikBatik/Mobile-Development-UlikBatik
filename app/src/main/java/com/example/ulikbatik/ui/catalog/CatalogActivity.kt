package com.example.ulikbatik.ui.catalog

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.databinding.ActivityCatalogBinding
import com.example.ulikbatik.ui.factory.CatalogViewModelFactory

class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private val catalogViewModel: CatalogViewModel by viewModels {
        CatalogViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setView()
        setAction()
    }

    private fun setAction() {
        binding.apply {
            backButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun setView() {
        catalogViewModel.apply {
            isLoading.observe(this@CatalogActivity) {
                showLoading(it)
            }

            dataCatalogs.observe(this@CatalogActivity) { res ->
                setData(res)
            }
        }
    }

    private fun setData(resData: GeneralResponse<List<BatikModel>>) {
        if (resData.data != null) {
            binding.apply {
                rvCatalog.layoutManager = GridLayoutManager(this@CatalogActivity, 3)
                rvCatalog.adapter = CatalogAdapter(resData.data)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

}