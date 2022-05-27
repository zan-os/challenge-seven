package com.example.challenge_seven.presentation.login.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.challenge_seven.common.Constant.TAG
import com.example.challenge_seven.databinding.ActivityLoginBinding
import com.example.challenge_seven.presentation.MainActivity
import com.example.challenge_seven.presentation.login.LoginViewModel
import com.example.challenge_seven.presentation.register.ui.RegisterActivity
import com.example.challenge_seven.utils.Extension.isEmailValid
import com.example.challenge_seven.utils.Extension.showLongToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        login()
        createAccount()
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            when {
                !email.isEmailValid() -> {
                    binding.emailEditText.error = "Email tidak valid"
                }
                else -> {
                    viewModel.login(email, password)

                    viewModel.state.observe(this) { result ->
                        if (result.result) {
                            navigateToMovieList()
                            viewModel.setUser(result.user?.email!!)
                            this.showLongToast("Login berhasil")
                        } else if (result.error.isNotEmpty()) {
                            this.showLongToast(result.error)
                        } else if (result.isLoading) {
                            showLoading(result.isLoading)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility
        }
    }

    private fun navigateToMovieList() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun createAccount() {
        binding.toRegisterButton.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}