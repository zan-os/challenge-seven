package com.example.challenge_seven.presentation.splashscreen.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_seven.common.Constant
import com.example.challenge_seven.databinding.ActivitySplashScreenBinding
import com.example.challenge_seven.presentation.MainActivity
import com.example.challenge_seven.presentation.login.ui.LoginActivity
import com.example.challenge_seven.presentation.splashscreen.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSplashScreen()
    }

    private fun startSplashScreen() {
        binding.logo.alpha = 0f
        binding.logo.animate().setDuration(3000).alpha(1f).withEndAction {
            checkCredential()
        }
    }

    private fun checkCredential() {
        viewModel.email.observe(this) { email ->
            if (email.isNotEmpty()) {
                Log.d(Constant.TAG, "email -> $email")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}