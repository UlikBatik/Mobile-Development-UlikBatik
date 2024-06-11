package com.example.ulikbatik.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.remote.response.PostItem
import com.example.ulikbatik.data.remote.response.ProfileUserResponse
import com.example.ulikbatik.databinding.ActivityProfileBinding
import com.example.ulikbatik.ui.factory.ProfileViewModelFactory
import com.example.ulikbatik.ui.profile.adapter.GridAdapter
import com.example.ulikbatik.ui.profile.adapter.ListAdapter

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private var userModel: UserModel? = null
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setViewModel()
        checkId()
    }

    private fun setViewModel() {
        profileViewModel.apply {
            userModel = user
        }
    }

    override fun onResume() {
        super.onResume()
        checkId()
    }

    private fun checkId() {
        val idOwnContent = intent.getStringExtra(EXTRA_ID_USER)
        val idGuestContent = intent.getStringExtra(EXTRA_ID_GUEST)
        val idUser = userModel?.uSERID

        if (idUser == idGuestContent || idUser == idOwnContent) {
            binding.buttonEditProfile.visibility = View.VISIBLE
            idUser?.let { setView(it) }
        } else {
            binding.buttonEditProfile.visibility = View.INVISIBLE
            idGuestContent?.let { setView(it) }
        }
    }

    private fun setView(idUser: String) {
        profileViewModel.getUserData(idUser).observe(this) { res ->
            if (res.status && res.data != null) {
                binding.apply {
                    res.data.let { dataProfile ->
                        Glide.with(root)
                            .load(dataProfile.pROFILEIMG)
                            .placeholder(R.drawable.ic_profile)
                            .into(imageProfile)
                        usernameProfile.text = dataProfile.uSERNAME
                        emailProfile.text = dataProfile.eMAIL
                        tvLikes.text = dataProfile.count?.likes.toString()
                        tvPosts.text = dataProfile.count?.post.toString()
                    }
                    showGridLayoutManager(true, res.data.post)
                    buttonGrid.setIconTintResource(R.color.primaryYellow)
                    buttonList.setIconTintResource(R.color.grey)
                    showPostsAction(res.data)
                }
            } else {
                handlePostError(res.message.toInt())
            }
        }

        binding.backButton.setOnClickListener {
            finish()
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

    private fun showPostsAction(data: ProfileUserResponse) {
        binding.apply {
            buttonGrid.setOnClickListener {
                showGridLayoutManager(true, data.post)
                buttonGrid.setIconTintResource(R.color.primaryYellow)
                buttonList.setIconTintResource(R.color.grey)
            }

            buttonList.setOnClickListener {
                showGridLayoutManager(false, data.post)
                buttonList.setIconTintResource(R.color.primaryYellow)
                buttonGrid.setIconTintResource(R.color.grey)
            }

            buttonEditProfile.setOnClickListener {
                val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun showGridLayoutManager(isGrid: Boolean, data: List<PostItem>) {
        binding.apply {
            if (isGrid) {
                rcPostProfile.layoutManager = GridLayoutManager(this@ProfileActivity, 2)
                val gridAdapter = GridAdapter(data)
                rcPostProfile.adapter = gridAdapter
            } else {
                rcPostProfile.layoutManager = LinearLayoutManager(this@ProfileActivity)
                val gridAdapter = ListAdapter(data)
                rcPostProfile.adapter = gridAdapter
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_ID_USER = "extra_id_user"
        const val EXTRA_ID_GUEST = "extra_id_guest"
    }
}