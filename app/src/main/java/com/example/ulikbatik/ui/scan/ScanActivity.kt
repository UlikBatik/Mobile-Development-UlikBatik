package com.example.ulikbatik.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
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
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.databinding.ActivityScanBinding
import com.example.ulikbatik.ui.factory.ScanViewModelFactory
import com.example.ulikbatik.ui.upload.UploadActivity
import com.example.ulikbatik.utils.helper.CameraHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yalantis.ucrop.UCrop
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private var currentImageUri: Uri? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private lateinit var resultScan: BatikModel
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

        scanViewModel.isLoading.observe(this) {
            showLoading(it)
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
                    if (res.status && res.result != null) {
                        resultScan = res.result


                        val height = 700
                        bottomSheetBehavior.peekHeight = height
                        bottomSheetBehavior.isHideable = false
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                        binding.apply {
                            bottomSheetPersistent.apply {
                                batikName.text = res.result.bATIKNAME
                                batikDesc.text = res.result.bATIKDESC
                                batikLoc.text = res.result.bATIKLOCT
                            }
                        }
                    } else {
                        handlePostError(res.message.toInt())
                    }
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

    private fun handlePostError(error: Int) {
        when (error) {
            400 -> {
                binding.apply {
                    resetBtn400.visibility = View.VISIBLE
                    showToast(getString(R.string.you_re_image_can_t_detected_as_batik))
                    resetBtn400.setOnClickListener {
                        camPreview.visibility = View.VISIBLE
                        camBtn.visibility = View.VISIBLE
                        galleryBtn.visibility = View.VISIBLE
                        resetBtn400.visibility = View.INVISIBLE

                        currentImageUri = null
                        imageIv.setImageURI(null)
                    }
                }
            }

            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
            503 -> showToast(getString(R.string.error_server_500))
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
            bottomSheetPersistent.resetBtn.setOnClickListener {
                camPreview.visibility = View.VISIBLE
                camBtn.visibility = View.VISIBLE
                galleryBtn.visibility = View.VISIBLE

                bottomSheetPersistent.resetBtn.visibility = View.INVISIBLE
                imageIv.visibility = View.INVISIBLE
                currentImageUri = null
                imageIv.setImageURI(null)
                bottomSheetBehavior =
                    BottomSheetBehavior.from(binding.bottomSheetPersistent.standardBottomSheet)
                bottomSheetBehavior.isHideable = true
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            bottomSheetPersistent.makePostBtn.setOnClickListener {
                val intent = Intent(this@ScanActivity, UploadActivity::class.java)
                intent.putExtra(UploadActivity.EXTRA_SCAN_IMAGE, currentImageUri.toString())
                intent.putExtra(UploadActivity.EXTRA_BATIK_NAME, resultScan.bATIKNAME)
                intent.putExtra(UploadActivity.EXTRA_BATIK_ID, resultScan.bATIKID)
                intent.putExtra(UploadActivity.EXTRA_BATIK_IMAGE, resultScan.bATIKIMG)
                intent.putExtra(UploadActivity.EXTRA_BATIK_LOCT, resultScan.bATIKLOCT)
                startActivity(intent)
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
            .withAspectRatio(9F, 12F)
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
                    output.savedUri?.let {
                        startUCropActivity(it)
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    showToast(getString(R.string.failed_to_took_picture))
                }
            }
        )
    }

    private fun showImage(uri: Uri) {
        binding.apply {
            camPreview.visibility = View.INVISIBLE
            camBtn.visibility = View.INVISIBLE
            galleryBtn.visibility = View.INVISIBLE

            bottomSheetPersistent.resetBtn.visibility = View.VISIBLE
            imageIv.visibility = View.VISIBLE
            imageIv.setImageURI(uri)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
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
