package com.example.ulikbatik.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.ulikbatik.R
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.databinding.ActivityUploadBinding
import com.example.ulikbatik.ui.factory.PostViewModelFactory
import com.example.ulikbatik.ui.factory.ScanViewModelFactory
import com.example.ulikbatik.ui.scan.ScanViewModel
import com.example.ulikbatik.ui.upload.CameraActivity.Companion.CAMERAX_RESULT
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.runBlocking
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null
    private var batikId: String? = null
    private var userIdValue: String? = null

    private val uploadViewModel: UploadViewModel by viewModels {
        PostViewModelFactory.getInstance(applicationContext)
    }
    private val scanViewModel: ScanViewModel by viewModels {
        ScanViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.uploadContainer)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isFromScan = intent.getStringExtra(EXTRA_BATIK_ID) != null
        if (isFromScan) {
            setViewFromScan()
        }
        setViewModel()
        setupAction()
    }

    private fun setViewFromScan() {
        val imageScan = intent.getStringExtra(EXTRA_SCAN_IMAGE)
        val batikName = intent.getStringExtra(EXTRA_BATIK_NAME)
        val batikLoc = intent.getStringExtra(EXTRA_BATIK_LOCT)
        val batikImage = intent.getStringExtra(EXTRA_BATIK_IMAGE)
        if (imageScan != null && batikName != null && batikLoc != null && batikImage != null) {
            batikId = intent.getStringExtra(EXTRA_BATIK_ID)
            currentImageUri = Uri.parse(imageScan)
            binding.apply {
                Glide.with(root)
                    .load(batikImage)
                    .into(batikTag.imgBatik)
                imageField.setImageURI(currentImageUri)
                batikTag.batikName.text = batikName
                batikTag.batikLoc.text = batikLoc
            }
        } else {
            showToast(getString(R.string.error_server_500))
        }
    }

    private fun setViewModel() {
        scanViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBarScan.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        uploadViewModel.apply {
            isLoading.observe(this@UploadActivity) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            userIdValue = userIdData
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            resultUri?.let {
                currentImageUri = it
                scanImage(it)
                showImage(it)
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            showToast(getString(R.string.error_server_500))
        }
    }

    private fun scanImage(image: Uri) {
        scanViewModel.scanImage(image, this).observe(this) { res ->
            if (res.status && res.result != null) {
                showScanResult(res.result)
                batikId = res.result.bATIKID
            } else {
                when (res.message.toInt()) {
                    200 -> {
                        showToast(getString(R.string.image_failed_to_detected))
                    }

                    400 -> {
                        showToast(getString(R.string.error_invalid_input))
                    }

                    401 -> {
                        showToast(getString(R.string.error_unauthorized_401))
                    }

                    500 -> {
                        showToast(getString(R.string.error_server_500))
                    }

                    503 -> {
                        showToast(getString(R.string.error_server_500))
                    }
                }
            }
        }
    }

    private fun showScanResult(res: BatikModel) {
        binding.apply {
            batikTag.batikName.text = res.bATIKNAME
            batikTag.batikLoc.text = res.bATIKLOCT
            Glide.with(this@UploadActivity).load(res.bATIKIMG)
                .placeholder(R.drawable.img_placeholder)
                .into(batikTag.imgBatik)
        }
    }

    private fun setupAction() {
        binding.apply {
            galleryBtn.setOnClickListener {
                startGallery()
            }
            cameraBtn.setOnClickListener {
                startCameraX()
            }
            uploadBtn.setOnClickListener {
                val caption = descriptionEdit.text.toString()
                val uri = currentImageUri
                val userId = userIdValue
                val batikId = batikId
                if (uri != null && userId != null && batikId != null) {
                    uploadViewModel.uploadPost(
                        uri, caption, userId, batikId, this@UploadActivity
                    ).observe(this@UploadActivity) { res ->
                        if (res.status && res.data != null) {
                            finish()
                        } else {
                            when (res.message.toInt()) {
                                400 -> {
                                    showToast(getString(R.string.error_invalid_input))
                                }

                                401 -> {
                                    showToast(getString(R.string.error_unauthorized_401))
                                }

                                500 -> {
                                    showToast(getString(R.string.error_server_500))
                                }

                                503 -> {
                                    showToast(getString(R.string.error_server_500))
                                }
                            }
                        }
                    }

                } else {
                    showToast(getString(R.string.error_server_500))
                }
            }
            backButton.setOnClickListener {
                finish()
            }
        }
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

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val requestPermissionCamera = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            showToast(getString(R.string.permission_granted))
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        } else {
            showToast(getString(R.string.permission_denied))
        }
    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            currentImageUri?.let { uri ->
                showImage(uri)
                scanImage(uri)
            }
        }
    }

    private fun startCameraX() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        } else {
            requestPermissionCamera.launch(Manifest.permission.CAMERA)
        }
    }


    private fun startUCropActivity(uri: Uri) {
        runBlocking {
            val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image"))
            UCrop.of(uri, destinationUri).withAspectRatio(9F, 16F).start(this@UploadActivity)
        }
    }


    private fun showImage(uri: Uri) {
        binding.apply {
            imageField.setImageURI(null)
            imageField.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val EXTRA_SCAN_IMAGE = "extra_scan_image"
        const val EXTRA_BATIK_NAME = "extra_batik_name"
        const val EXTRA_BATIK_LOCT = "extra_batik_loct"
        const val EXTRA_BATIK_IMAGE = "extra_batik_image"
        const val EXTRA_BATIK_ID = "extra_batik_id"
    }
}