package com.example.ulikbatik.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ulikbatik.R
import com.example.ulikbatik.databinding.ActivityScanBinding
import com.example.ulikbatik.utils.helper.CameraHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yalantis.ucrop.UCrop
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private val scanViewModel: ScanViewModel by viewModels {
        ScanViewModelFactory.getInstance(applicationContext)
    }


    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetPersistent.standardBottomSheet)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        scanViewModel.apply {
            isLoading.observe(this@ScanActivity) {
                showLoading(it)
            }
        }

        setupAction()
        startCameraX()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            resultUri?.let {
                currentImageUri = it
                scanViewModel.scanImage(it, this).observe(this) { res ->
                    bottomSheetBehavior.isHideable = false
                    bottomSheetBehavior.peekHeight = 700
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    binding.bottomSheetPersistent.batikName.text = res.result
                }
                showImage(it)
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let {
                showToast("Crop error: ${it.localizedMessage}")
            }
        }
    }

    private fun setupAction() {
        binding.apply {
            galleryBtn.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            backButton.setOnClickListener {
                finish()
            }
            camBtn.setOnClickListener {
                tookPhoto()
            }
            resetBtn.setOnClickListener {
                camPreview.visibility = android.view.View.VISIBLE
                camBtn.visibility = android.view.View.VISIBLE
                galleryBtn.visibility = android.view.View.VISIBLE

                resetBtn.visibility = android.view.View.INVISIBLE
                imageIv.visibility = android.view.View.INVISIBLE
                currentImageUri = null
                imageIv.setImageURI(null)
            }
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            startUCropActivity(uri)
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private val requestPermissionCamera =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast(getString(R.string.permission_granted))
                startCamera()
            } else {
                showToast(getString(R.string.permission_denied))
            }
        }

    private fun startUCropActivity(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(9F, 16F)
            .start(this)
    }

    private fun startCameraX() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionCamera.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.camPreview.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                showToast(getString(R.string.failed_to_use_camera))
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private fun tookPhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = CameraHelper.createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    currentImageUri = output.savedUri
                    output.savedUri?.let { startUCropActivity(it) }
                }

                override fun onError(exc: ImageCaptureException) {
                    showToast(getString(R.string.failed_to_took_picture))
                }
            }
        )
    }

    private fun showImage(uri: Uri) {
        binding.apply {
            camPreview.visibility = android.view.View.INVISIBLE
            camBtn.visibility = android.view.View.INVISIBLE
            galleryBtn.visibility = android.view.View.INVISIBLE

            resetBtn.visibility = android.view.View.VISIBLE
            imageIv.visibility = android.view.View.VISIBLE
            imageIv.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
    }


    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }
}
