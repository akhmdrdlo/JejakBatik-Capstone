package com.example.jejak_batik.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.ActivityVerifyBinding
import com.example.jejak_batik.di.Result
import com.example.jejak_batik.di.ViewModelFactory
import com.example.jejak_batik.ui.login.LoginActivity

class VerifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyBinding
    private val viewModel: VerifyViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email") ?: ""
        val name = intent.getStringExtra("name") ?: ""

        setupAction(email, name)
    }

    private fun setupAction(email: String, name: String) {
        val password = intent.getStringExtra("password") ?: ""

        binding.verivikasiButton.setOnClickListener {
            val otp = binding.edOtp.text.toString()

            if (isOtpValid(otp)) {
                viewModel.verify(email, otp, name, password).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            navigateToLogin()
                        }
                        is Result.Error -> {
                            showLoading(false)
                            if (result.message.contains("kadaluarsa", ignoreCase = true)) {
                                showResendOtpDialog(email)
                            } else {
                                handleVerifyError(result.message)
                            }
                        }
                    }
                }
            } else {
                binding.edOtp.error = getString(R.string.invalid_otp_format)
            }
        }
    }

    private fun showResendOtpDialog(email: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.otp_expired))
            setMessage(getString(R.string.resend_otp_message))
            setPositiveButton(getString(R.string.resend)) { _, _ ->
                resendOtp(email)
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun resendOtp(email: String) {
        viewModel.resendOtp(email).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    Log.d("VerifyActivity", "Resending OTP...")
                }
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, getString(R.string.otp_resend_success), Toast.LENGTH_SHORT).show()
                    Log.d("VerifyActivity", "OTP resent successfully.")
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    Log.e("VerifyActivity", "Error resending OTP: ${result.message}")
                }
            }
        }
    }

    private fun handleVerifyError(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.verify_failed_title))
            setMessage(message)
            setPositiveButton(getString(R.string.retry)) { dialog, _ ->
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.back_to_signup)) { dialog, _ ->
                navigateToSignup()
            }
            create()
            show()
        }
    }

    private fun isOtpValid(otp: String): Boolean {
        return otp.length == 6 && otp.all { it.isDigit() }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.dimBackground.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
