package com.example.ulikbatik.ui.likes

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ulikbatik.R
import com.example.ulikbatik.data.LikesDummy
import com.example.ulikbatik.databinding.ActivityLikesBinding

class LikesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLikesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLikesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setView()
    }

    private fun setView() {
        val likes = LikesDummy.getLikes()
        binding.rvLikes.layoutManager = LinearLayoutManager(this)
        binding.rvLikes.adapter = LikesAdapter(likes)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}