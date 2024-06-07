package com.example.ulikbatik.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.databinding.ActivityAuthBinding
import com.example.ulikbatik.ui.dashboard.DashboardActivity
import com.example.ulikbatik.ui.factory.AuthViewModelFactory
import com.example.ulikbatik.ui.likes.LikesViewModel
import com.example.ulikbatik.ui.likes.LikesViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var preferences: UserPreferences
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.authContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setViewModel()
        setView()

    }

    private fun setView() {
        val token = runBlocking {
            preferences.getUserToken().first()
        }

        if (token != null) {
            val intent = Intent(this@AuthActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.authContainer, LoginFragment.newInstance())
                .commit()
        }
    }

    private fun setViewModel() {
        preferences = authViewModel.pref
    }
}
