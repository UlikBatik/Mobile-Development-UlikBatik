package com.example.ulikbatik.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.example.ulikbatik.ml.ModelMnet
import com.example.ulikbatik.ui.factory.PostViewModelFactory
import com.example.ulikbatik.ui.factory.ScanViewModelFactory
import com.example.ulikbatik.ui.scan.ScanViewModel
import com.example.ulikbatik.ui.upload.CameraActivity.Companion.CAMERAX_RESULT
import com.example.ulikbatik.utils.helper.CameraHelper
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer

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
            userIdValue = user?.uSERID
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
        val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(image))
        val bitmapGrayScale = CameraHelper.convertToGrayscale(bitmap)
        val resizedImage = CameraHelper.resizeBitmap(bitmapGrayScale, 224, 224)
        val byteBuffer = CameraHelper.bitmapToByteBuffer(resizedImage)
        classifyGenerator(byteBuffer)
    }

    private fun classifyGenerator(byteBuffer: ByteBuffer) {
        val model = ModelMnet.newInstance(this)

        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        model.close()

        handleOutput(outputFeature0)
    }

    private fun handleOutput(outputFeature0: TensorBuffer) {

        val probabilities = outputFeature0.floatArray

        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1
        val maxProbability = probabilities[maxIndex]

        val labels = arrayOf("BTX00",
            "BTX01", "BTX02", "BTX03", "BTX04", "BTX05",
            "BTX06", "BTX07", "BTX08", "BTX09", "BTX10",
            "BTX11", "BTX12", "BTX13", "BTX14", "BTX15",
            "BTX16", "BTX17", "BTX18", "BTX19", "BTX20",
            "BTX21", "BTX22"
        )

        val predictedLabel =
            if (maxIndex < labels.size) labels[maxIndex] else "Unknown"

        scanViewModel.getBatikTag(predictedLabel).observe(this@UploadActivity) { res ->
            if (res.status && res.data != null) {
                showScanResult(res.data)
            } else {
                handlePostError(res.message.toInt())
            }
        }


        Log.d("COBA","$predictedLabel $maxProbability")
    }


    private fun handlePostError(error: Int) {
        when (error) {
            400 -> showToast(getString(R.string.error_invalid_input))
            401 -> showToast(getString(R.string.error_unauthorized_401))
            500 -> showToast(getString(R.string.error_server_500))
            503 -> showToast(getString(R.string.error_server_500))
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
                            handlePostError(res.message.toInt())
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
            UCrop.of(uri, destinationUri).withAspectRatio(3F, 3F).start(this@UploadActivity)
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