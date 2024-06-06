package com.example.ulikbatik.ui.likes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ulikbatik.R
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.local.dataStore
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.databinding.ActivityLikesBinding
import com.example.ulikbatik.ui.dashboard.DashboardAdapter
import kotlinx.coroutines.launch

class LikesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLikesBinding

    private val likesViewModel: LikesViewModel by viewModels {
        LikesViewModelFactory.getInstance(applicationContext)
    }

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
        val pref = UserPreferences.getInstance(this.dataStore)
        lifecycleScope.launch{
            pref.getUserId().collect{userId ->
                if (userId != null) {

                    likesViewModel.getLikes(userId).observe(this@LikesActivity){
                        if (it != null){
                            setView(it)
                        }
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val pref = UserPreferences.getInstance(this.dataStore)
        lifecycleScope.launch{
            pref.getUserId().collect{userId ->
                if (userId != null) {

                    likesViewModel.getLikes(userId).observe(this@LikesActivity){
                        if (it != null){
                            setView(it)
                        }
                    }
                }
            }
        }
    }

    private fun setView(res: GeneralResponse<List<LikesModel>>) {
        if(res.data != null) {
            binding.apply {
                binding.rvLikes.layoutManager = LinearLayoutManager(this@LikesActivity)
                binding.rvLikes.adapter = LikesAdapter(res.data)
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}