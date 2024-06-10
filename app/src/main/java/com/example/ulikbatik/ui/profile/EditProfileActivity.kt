package com.example.ulikbatik.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.ActivityEditProfileBinding
import com.example.ulikbatik.ui.factory.ProfileViewModelFactory
import com.yalantis.ucrop.UCrop
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var currentImageUri: Uri
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setupView()
        setupAction()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage(resultUri)
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let {
                showToast("Crop error: ${it.localizedMessage}")
            }
        }
    }

    private fun showImage(image: Uri) {
        binding.imageView.setImageURI(image)
    }

    private fun setupView() {
        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val prevUsername = intent.getStringExtra(EXTRA_PROFILE_USERNAME)
        val prevImage = intent.getStringExtra(EXTRA_PROFILE_IMAGE)
        if (prevUsername != null && prevImage != null) {
            currentImageUri = Uri.parse(prevImage)
            binding.apply {
                editUsername.setText(prevUsername)
                Glide.with(root)
                    .load(prevImage)
                    .into(imageView)
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            editImage.setOnClickListener {
                startGallery()
            }

            saveProfileBtn.setOnClickListener {
                saveProfile()
            }

            backButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun saveProfile() {
        val newUsername = binding.editUsername.text.toString()
        if (newUsername.isEmpty()) {
            showToast(getString(R.string.username_should_not_empty))
            return
        } else {
            val idUser = profileViewModel.userIdData
            if (idUser != null) {
                profileViewModel.saveProfileUser(currentImageUri, newUsername, idUser, this)
                    .observe(this) { res ->
                        if (res.status) {
                            finish()
                        } else {
                            showToast(getString(R.string.error_server_500))
                        }
                    }
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            startUCropActivity(uri)
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private fun startUCropActivity(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1F, 1F)
            .start(this)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }

    companion object {
        const val EXTRA_PROFILE_USERNAME = "extra_profile_username"
        const val EXTRA_PROFILE_IMAGE = "extra_profile_image"
    }
}