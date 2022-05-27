package com.example.challenge_seven.presentation.profile.ui

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.challenge_seven.R
import com.example.challenge_seven.databinding.FragmentProfileBinding
import com.example.challenge_seven.domain.model.User
import com.example.challenge_seven.presentation.login.ui.LoginActivity
import com.example.challenge_seven.presentation.profile.ProfileViewModel
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
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private var user: User? = null

    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if (result != null) {
                requireContext().loadImage(result, binding.profileImage)
            }
        }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                requireContext().loadImage(bitmap, binding.profileImage)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserEmail()
        logout()
        updateUser()
        moveToMovieList()
        chooseImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getUserEmail() {
        viewModel.email.observe(viewLifecycleOwner) { email ->
            findUserData(email)
        }
    }

    private fun findUserData(email: String) {
        viewModel.getUser(email)
        getUserData()
    }

    private fun getUserData() {
        viewModel.userData.observe(viewLifecycleOwner) { result ->
            showUserData(result.user)
            user = result.user
        }
    }

    private fun showUserData(user: User?) {
        binding.apply {
            usernameEditText.setText(user?.username)
            fullNameEditText.setText(user?.name)
            dateOfBirthEditText.setText(user?.dateOfBirth)
            addressEditText.setText(user?.address)
            profileImage.load(user?.profilePhoto)
        }
    }

    private fun updateUser() {
        binding.updateButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val name = binding.fullNameEditText.text.toString().trim()
            val date = binding.dateOfBirthEditText.text.toString().trim()
            val address = binding.addressEditText.text.toString().trim()
            val profilePhoto = binding.profileImage.drawable.toBitmap()

            user?.let {
                it.username = username
                it.name = name
                it.dateOfBirth = date
                it.address = address
                it.profilePhoto = profilePhoto

                when {
                    !username.isUsernameValid() -> {
                        binding.usernameEditText.error = "Username minimal 3 karakter"
                    }
                    else -> {
                        viewModel.updateUser(it)
                        viewModel.updateResult.observe(viewLifecycleOwner) { result ->
                            if (result.result == 1) {
                                requireContext().showLongToast("Berhasil diupdate")
                            } else if (result.error.isNotEmpty()) {
                                requireContext().showLongToast("Gagal diupdate, ${result.error}")
                            }
                        }
                    }
                }
            }

        }
    }

    private fun moveToMovieList() {
        binding.toolbarId.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun logout() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun chooseImage() {
        binding.profileImage.setOnClickListener {
            cameraCheckPermission()
        }
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setMessage("Required permission for this feature")
            .setPositiveButton("Go to settings") { _, _ ->
                try {
                    val intent = Intent()
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)

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

    private fun cameraCheckPermission() {
        Dexter.withContext(requireContext())
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
            .withErrorListener {
                requireContext().showLongToast(it.name)
            }
            .check()
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(requireContext(), R.style.AlertDialogTheme)
            .setMessage("Choose an Image")
            .setPositiveButton("Gallery") { _, _ -> gallery() }
            .setNegativeButton("Camera") { _, _ -> camera() }
            .show()
    }

    private fun gallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun camera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraResult.launch(it) }
    }
}