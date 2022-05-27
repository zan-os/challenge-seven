package com.example.challenge_seven.presentation.register.ui

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.challenge_seven.R
import com.example.challenge_seven.databinding.ActivityRegisterBinding
import com.example.challenge_seven.presentation.login.ui.LoginActivity
import com.example.challenge_seven.presentation.register.RegisterViewModel
import com.example.challenge_seven.utils.Extension.isEmailValid
import com.example.challenge_seven.utils.Extension.isPasswordValid
import com.example.challenge_seven.utils.Extension.isUsernameValid
import com.example.challenge_seven.utils.Extension.loadImage
import com.example.challenge_seven.utils.Extension.showLongToast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                loadImage(result, binding.profileImage)
            }
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                loadImage(bitmap, binding.profileImage)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        register()
        alreadyHaveAccount()
        chooseImage()
    }

    private fun register() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val username = binding.usernameEditText.text.toString().trim()
            val profilePhoto = binding.profileImage.drawable.toBitmap()

            when {
                !email.isEmailValid() -> {
                    binding.emailEditText.error = "Email tidak valid"
                }
                !password.isPasswordValid() -> {
                    binding.passwordEditText.error = "Password minimal 6 karakter"
                }
                confirmPassword != password -> {
                    binding.confirmPasswordEditText.error = "Password tidak sesuai"
                }
                !username.isUsernameValid() -> {
                    binding.usernameEditText.error = "Username minimal 3 karakter"
                }
                else -> {
                    viewModel.register(username, email, password, profilePhoto)

                    viewModel.state.observe(this) { result ->
                        if (result.user == 0L) {
                            this.showLongToast("Something went wrong")
                        } else {
                            this.showLongToast("Resgistrasi berhasil, silahkan login")
                            navigateToLogin()
                        }
                    }
                }
            }
        }
    }

    private fun alreadyHaveAccount() {
        binding.toLoginButton.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun chooseImage() {
        binding.profileImage.setOnClickListener { cameraCheckPermission() }
    }

    private fun cameraCheckPermission() {
        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(reports: MultiplePermissionsReport?) {
                    reports?.let {
                        if (reports.areAllPermissionsGranted()) {
                            chooseImageDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                    showRotationalDialogForPermission()
                }
            })
            .onSameThread()
            .withErrorListener() {
                this.showLongToast(it.name)
            }
            .check()
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage("Required permission for this feature")
            .setPositiveButton("Go to settings") { _, _ ->
                try {
                    val intent = Intent()
                    val uri = Uri.fromParts("package", this.packageName, null)

                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = uri

                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setMessage("Choose an Image")
            .setPositiveButton("Gallery") { _, _ -> gallery() }
            .setNegativeButton("Camera") { _, _ -> camera() }
            .show()
    }

    private fun gallery() {
        this.intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun camera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraResult.launch(it) }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}