package com.example.jejak_batik.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.ActivitySignupBinding
import com.example.jejak_batik.di.Result
import com.example.jejak_batik.di.ViewModelFactory

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            if (isInputValid(name, email, password)) {
                viewModel.register(name, email, password).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            navigateToVerify(email, name, password)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            if (result.message.contains("Email sudah didaftarkan", ignoreCase = true)) {
                                showAlreadyRegisteredDialog(email, name, password)
                            } else {
                                showErrorDialog(result.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Kesalahan")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun showAlreadyRegisteredDialog(email: String, name: String, password: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Email Sudah Terdaftar")
            setMessage("Email ini sudah terdaftar. Silakan lanjutkan proses verifikasi.")
            setPositiveButton("Lanjutkan Verifikasi") { _, _ ->
                navigateToVerify(email, name, password)
            }
            setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun isInputValid(name: String, email: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.edRegisterName.error = getString(R.string.name_error)
            isValid = false
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edRegisterEmail.error = getString(R.string.email_invalid_format)
            isValid = false
        }
        if (password.length < 8) {
            binding.edRegisterPassword.error = getString(R.string.password_error)
            isValid = false
        }

        return isValid
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.dimBackground.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToVerify(email: String, name: String, password: String) {
        val intent = Intent(this, VerifyActivity::class.java).apply {
            putExtra("email", email)
            putExtra("name", name)
            putExtra("password", password)
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animations = listOf(
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f)
        )

        AnimatorSet().apply {
            playSequentially(animations)
            startDelay = 100
        }.start()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

