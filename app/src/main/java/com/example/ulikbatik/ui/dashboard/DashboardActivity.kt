package com.example.ulikbatik.ui.dashboard


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.databinding.ActivityDashboardBinding
import com.example.ulikbatik.ui.catalog.CatalogActivity
import com.example.ulikbatik.ui.likes.LikesActivity
import com.example.ulikbatik.ui.profile.ProfileActivity
import com.example.ulikbatik.ui.scan.ScanActivity
import com.example.ulikbatik.ui.upload.UploadActivity
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels {
        PostViewModelFactory.getInstance(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setDrawer()
        setViewModel()
        setAction()

    }

    private fun setViewModel() {
        dashboardViewModel.apply{
            isLoading.observe(this@DashboardActivity) {
                showLoading(it)
            }

            allPost.observe(this@DashboardActivity){
                if (it != null){
                    setView(it)
                }
            }
        }
    }

    private fun setAction(){
        binding.apply{
            contentDashboard.scanBtn.setOnClickListener{
                val intent = Intent(this@DashboardActivity, ScanActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.catalogBtn.setOnClickListener{
                val intent = Intent(this@DashboardActivity, CatalogActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.likesBtn.setOnClickListener{
                val intent = Intent(this@DashboardActivity, LikesActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.profileBtn.setOnClickListener{
                val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.dashboardFab.setOnClickListener{
                val intent = Intent(this@DashboardActivity, UploadActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setView(res: GeneralResponse<List<PostModel>>) {

        if(res.data != null) {
            binding.apply {
                contentDashboard.rvPost.layoutManager =
                    LinearLayoutManager(this@DashboardActivity)
                contentDashboard.rvPost.adapter = DashboardAdapter(res.data)
            }
        }

        var pref = UserPreferences.getInstance(this.dataStore)

        lifecycleScope.launch {
            pref.getUsername().collect{ username ->
                binding.contentDashboard.usernameTv.text = username
            }
        }
    }

    private fun setDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_darkmode, R.id.nav_my_account, R.id.nav_language, R.id.nav_logout
            ), drawerLayout
        )

        binding.contentDashboard.menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_language -> {
                return true
            }
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentDashboard.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }


}