package com.example.ulikbatik.ui.splashscreen

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
import com.example.ulikbatik.ui.auth.AuthActivity
import com.example.ulikbatik.ui.boarding.BoardingActivity
import com.example.ulikbatik.ui.dashboard.DashboardActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
            delay(5000)

            val isSession = pref.getSession().first()

            when {
                !isSession -> {
                    startActivity(Intent(this@SplashscreenActivity, BoardingActivity::class.java))
                }
                else -> {
                    startActivity(Intent(this@SplashscreenActivity, AuthActivity::class.java))
                }
            }
            finish()
        }
    }
}
