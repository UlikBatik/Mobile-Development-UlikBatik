package com.example.ulikbatik.ui.dashboard


import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.databinding.ActivityDashboardBinding
import com.example.ulikbatik.ui.auth.AuthActivity
import com.example.ulikbatik.ui.catalog.CatalogActivity
import com.example.ulikbatik.ui.customView.CustomDialog
import com.example.ulikbatik.ui.factory.PostViewModelFactory
import com.example.ulikbatik.ui.likes.LikesActivity
import com.example.ulikbatik.ui.profile.ProfileActivity
import com.example.ulikbatik.ui.scan.ScanActivity
import com.example.ulikbatik.ui.upload.UploadActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var preferences: UserPreferences
    private var userModel: UserModel? = null

    private val dashboardViewModel: DashboardViewModel by viewModels {
        PostViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.contentDashboard.rvPost.layoutManager =
            LinearLayoutManager(this)

        setViewModel()
        setDrawer()
        setAction()
        setView()
    }

    override fun onResume() {
        super.onResume()
        setViewModel()
        setDrawer()
        setView()
    }

    private fun setViewModel() {
        dashboardViewModel.apply {
            preferences = pref
            userModel = user

            isLoading.observe(this@DashboardActivity) {
                showLoading(it)
            }
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
                val idUser = dashboardViewModel.user?.uSERID
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
            contentDashboard.scrollUpFab.setOnClickListener {
                contentDashboard.rvPost.scrollToPosition(0)
                contentDashboard.appBar.setExpanded(true, true)
                setView()
            }
        }
    }

    private fun setView() {
        val adapter = DashboardAdapter()
        binding.contentDashboard.rvPost.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        dashboardViewModel.getPosts().observe(this@DashboardActivity) { dataPaging ->
            adapter.submitData(lifecycle, dataPaging)
        }

        lifecycleScope.launch {
            preferences.getUser().collect {
                binding.apply {
                    contentDashboard.usernameTv.text = it?.uSERNAME
                    Glide.with(root)
                        .load(it?.pROFILEIMG)
                        .placeholder(R.drawable.ic_profile)
                        .into(contentDashboard.profileBtn)
                }
            }
        }

        binding.contentDashboard.apply {
            binding.contentDashboard.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (verticalOffset == 0) {
                    scrollUpFab.hide()
                } else if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                    scrollUpFab.show()
                }
            }
        }
    }

    private fun setDrawer() {
        setSupportActionBar(binding.contentDashboard.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.apply {
            contentDashboard.toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_my_account, R.id.nav_language, R.id.nav_logout
            ), drawerLayout
        )


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
        val idUser = dashboardViewModel.user?.uSERID
        if (idUser != null) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
            intent.putExtra(ProfileActivity.EXTRA_ID_USER, idUser)
            startActivity(intent)
        }
    }


    private fun logout() {

        val title = getString(R.string.logout)
        val message = getString(R.string.logout_from_your_account)
        val customDialog = CustomDialog(this)
        customDialog.showDialog(title, message) { userChoice ->
            if (userChoice) {
                lifecycleScope.launch {
                    delay(3000)
                    preferences.logOut()

                    val intent = Intent(this@DashboardActivity, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun changeLanguage() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.contentDashboard.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

}