package com.example.jejak_batik.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jejak_batik.ui.main.MainActivity
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.ActivityLoginBinding
import com.example.jejak_batik.di.Result
import com.example.jejak_batik.di.ViewModelFactory
import com.example.jejak_batik.ui.history.LocalHistoryActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            if (result.data.status) {
                                saveToken(
                                    result.data.nama,
                                    result.data.email,
                                    result.data.kode_token
                                )
                                navigateToMain()
                            } else {
                                showErrorDialog(result.data.message ?: "Login gagal")
                            }
                        }
                        is Result.Error -> {
                            showLoading(false)
                            showErrorDialog(result.message ?: "Terjadi kesalahan. Silakan coba lagi.")
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.invalid_input_message), Toast.LENGTH_SHORT).show()
            }
        }
        binding.historyButton.setOnClickListener {
            val intent = Intent(this, LocalHistoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.forgetTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }

    private fun showErrorDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Login Gagal")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animations = listOf(
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.historyButton, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.forgetTextView, View.ALPHA, 1f),
        )

        AnimatorSet().apply {
            playSequentially(animations)
            startDelay = 100
        }.start()
    }

    private fun saveToken(name: String, email: String, token: String) {
        Log.d("LoginActivity", "Saving token: $token")
        viewModel.saveToken(name, email, token)
    }

    private fun navigateToMain() {
        Log.d("LoginActivity", "Navigating to MainActivity")
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.dimBackground.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
