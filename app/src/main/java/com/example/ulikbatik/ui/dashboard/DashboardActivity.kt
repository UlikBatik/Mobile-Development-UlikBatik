package com.example.ulikbatik.ui.dashboard


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.provider.Settings
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.databinding.ActivityDashboardBinding
import com.example.ulikbatik.ui.auth.AuthActivity
import com.example.ulikbatik.ui.catalog.CatalogActivity
import com.example.ulikbatik.ui.factory.PostViewModelFactory
import com.example.ulikbatik.ui.likes.LikesActivity
import com.example.ulikbatik.ui.profile.ProfileActivity
import com.example.ulikbatik.ui.scan.ScanActivity
import com.example.ulikbatik.ui.upload.UploadActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var preferences: UserPreferences
    private val dashboardViewModel: DashboardViewModel by viewModels {
        PostViewModelFactory.getInstance(applicationContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDrawer()
        setViewModel()
        setAction()
    }

    override fun onResume() {
        super.onResume()
        setViewModel()
        setDrawer()
    }

    private fun setViewModel() {
        dashboardViewModel.apply {

            preferences = pref

            isLoading.observe(this@DashboardActivity) {
                showLoading(it)
            }

            dashboardViewModel.getPosts().observe(this@DashboardActivity) {
                if (it != null) {
                    if (it.status) {
                        setView(it)
                    } else {
                        handlePostError(it.message.toInt())
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
        }
    }

    private fun setAction() {
        binding.apply {
            contentDashboard.scanBtn.setOnClickListener {
                val intent = Intent(this@DashboardActivity, ScanActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.catalogBtn.setOnClickListener {
                val intent = Intent(this@DashboardActivity, CatalogActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.likesBtn.setOnClickListener {
                val intent = Intent(this@DashboardActivity, LikesActivity::class.java)
                startActivity(intent)
            }

            contentDashboard.profileBtn.setOnClickListener {
                val idUser = dashboardViewModel.userIdData
                if (idUser != null) {
                    val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
                    intent.putExtra(ProfileActivity.EXTRA_ID_USER, idUser)
                    startActivity(intent)
                }
            }

            contentDashboard.dashboardFab.setOnClickListener {
                val intent = Intent(this@DashboardActivity, UploadActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setView(res: GeneralResponse<List<PostModel>>) {
        if (res.data != null) {
            binding.apply {
                contentDashboard.rvPost.layoutManager =
                    LinearLayoutManager(this@DashboardActivity)
                contentDashboard.rvPost.adapter = DashboardAdapter(res.data)
            }
        }

        lifecycleScope.launch {
            preferences.getUser().collect {
                if (it != null) {
                    binding.apply {
                        contentDashboard.usernameTv.text = it.uSERNAME
                        Glide.with(root)
                            .load(it.pROFILEIMG)
                            .placeholder(R.drawable.ic_profile)
                            .into(contentDashboard.profileBtn)
                    }
                }
            }
        }
        binding.apply {
            contentDashboard.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY == oldScrollY || scrollY == 0) {
                    contentDashboard.scrollUpFab.hide()
                } else {
                    contentDashboard.scrollUpFab.show()
                }
            }

            contentDashboard.scrollUpFab.setOnClickListener {
                setViewModel()
                contentDashboard.nestedScrollView.smoothScrollTo(0, 0)
            }
        }
    }

    private fun setDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_my_account, R.id.nav_language, R.id.nav_logout
            ), drawerLayout
        )

        binding.contentDashboard.menuBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    logout()
                   false
                }

                R.id.nav_language -> {
                    changeLanguage()
                  false
                }

                R.id.nav_my_account -> {
                    goToAccount()
                   false
                }

                else -> false
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                finish()
            }
        }
    }

    private fun goToAccount() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    private fun logout() {
        lifecycleScope.launch {
            delay(3000)
            preferences.logOut()

            val intent = Intent(this@DashboardActivity, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun changeLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentDashboard.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}