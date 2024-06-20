package com.example.ulikbatik.ui.catalog

import android.os.Bundle
import android.widget.Toast
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
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private val catalogViewModel: CatalogViewModel by viewModels {
        CatalogViewModelFactory.getInstance(applicationContext)
    }

    private lateinit var searchBar: SearchBar
    private lateinit var searchView: SearchView

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

        searchBar = binding.searchBar
        searchView = binding.searchView

        setView()
        setAction()
        setupSearch()
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

                noResultTv.visibility =
                    if (resData.data.isEmpty()) android.view.View.VISIBLE else android.view.View.INVISIBLE

                rvCatalog.layoutManager = GridLayoutManager(this@CatalogActivity, 3)
                rvCatalog.adapter = CatalogAdapter(resData.data)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun setupSearch() {
        searchView.setupWithSearchBar(searchBar)
        searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = searchView.editText.text.toString()
            searchBar.setText(query)
            if (query.isNotEmpty()) {
                searchBatik(query)
            } else {
                setView()
            }
            searchView.hide()
            true
        }
    }

    private fun searchBatik(query: String) {
        catalogViewModel.searchCatalog(query).observe(this) { res ->
            if (res.status) {
                setData(res)
            } else {
                handlePostError(res.message.toInt())
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
