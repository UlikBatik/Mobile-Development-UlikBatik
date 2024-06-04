package com.example.ulikbatik.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.databinding.ActivityAuthBinding
import com.example.ulikbatik.ui.dashboard.DashboardActivity
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthBinding

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

        val pref = UserPreferences.getInstance(this.dataStore)

        lifecycleScope.launch {
            pref.getUserToken().collect{
                if(it==null){
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.authContainer, LoginFragment.newInstance())
                        .commit()
                } else {
                    val intent = Intent(this@AuthActivity, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}