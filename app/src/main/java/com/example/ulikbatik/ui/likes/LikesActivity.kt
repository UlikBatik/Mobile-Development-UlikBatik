package com.example.ulikbatik.ui.likes

import android.os.Bundle
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
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.databinding.ActivityLikesBinding
import com.example.ulikbatik.ui.factory.LikesViewModelFactory
import kotlinx.coroutines.launch

class LikesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLikesBinding
    private var userModel: UserModel? = null
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

        setViewModel()
        setLikes()
    }

    private fun setViewModel() {
       likesViewModel.apply {
           userModel = user

            isLoading.observe(this@LikesActivity){
                showLoading(it)
            }

       }
    }

    override fun onResume() {
        super.onResume()
         setLikes()
    }

    private fun setLikes() {
        userModel?.uSERID?.let { userId ->
            likesViewModel.getLikes(userId).observe(this@LikesActivity) { response ->
                if (response.status) {
                    setView(response)
                    binding.noLikesTv.visibility = if (response.data.isNullOrEmpty()) android.view.View.VISIBLE else android.view.View.GONE
                } else {
                    handlePostError(response.message.toInt())
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

    private fun handlePostError(error: Int){
        when (error) {
            400 -> showToast(getString(R.string.error_invalid_input))
            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }
}