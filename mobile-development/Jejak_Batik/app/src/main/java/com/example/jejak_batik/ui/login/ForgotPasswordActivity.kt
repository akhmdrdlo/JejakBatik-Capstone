package com.example.jejak_batik.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jejak_batik.R
import com.example.jejak_batik.data.api.ApiConfig
import com.example.jejak_batik.data.model.auth.ForgetPasswordRequest
import com.example.jejak_batik.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.forgetButton.setOnClickListener {
            val email = binding.edForget.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isEmailValid(email)) {
                Toast.makeText(this, "Masukkan email yang valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendForgetPasswordRequest(email)
        }
    }

    private fun sendForgetPasswordRequest(email: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.dimBackground.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiService = ApiConfig.getApiService(this@ForgotPasswordActivity)
                val request = ForgetPasswordRequest(email)
                val response = apiService.forgetPassword(request)

                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.dimBackground.visibility = View.GONE

                    if (response.status) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            response.message,
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.dimBackground.visibility = View.GONE
                    // Jika kode respons 404, berarti email tidak terdaftar.
                    if (e.code() == 404) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Email ini belum terdaftar. Silakan buat akun terlebih dahulu.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Terjadi kesalahan: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                    binding.dimBackground.visibility = View.GONE
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Terjadi kesalahan: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
