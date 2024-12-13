package com.example.jejak_batik.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.jejak_batik.R
import com.example.jejak_batik.data.api.ApiConfig
import com.example.jejak_batik.data.pref.UserPreference
import com.example.jejak_batik.data.pref.dataStore
import com.example.jejak_batik.ui.main.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var userPreference: UserPreference
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var deleteAccountButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        userPreference = UserPreference.getInstance(requireContext().dataStore)

        userNameTextView = rootView.findViewById(R.id.user_name)
        userEmailTextView = rootView.findViewById(R.id.user_email)
        logoutButton = rootView.findViewById(R.id.logout_button)
        deleteAccountButton = rootView.findViewById(R.id.delete_account_button)

        setupUserInfo()
        setupLogoutButton()
        setupDeleteAccountButton()

        return rootView
    }

    private fun setupUserInfo() {
        lifecycleScope.launch {
            val session = userPreference.getSession().first()
            userNameTextView.text = " ${session.name}"
            userEmailTextView.text = " ${session.email}"
        }
    }

    private fun setupLogoutButton() {
        logoutButton.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupDeleteAccountButton() {
        deleteAccountButton.setOnClickListener {
            showDeleteAccountDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin logout?")
            .setPositiveButton("Ya") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Akun")
            .setMessage("Apakah Anda yakin ingin menghapus akun Anda? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Ya") { _, _ ->
                performDeleteAccount()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun performLogout() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                userPreference.logout()
                navigateToWelcome()
            } catch (e: Exception) {
                showToastOnMainThread("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun performDeleteAccount() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val email = userPreference.getEmail().first() ?: ""
                if (email.isEmpty()) {
                    showToastOnMainThread("Gagal menghapus akun: Email tidak tersedia.")
                    return@launch
                }
                val apiService = ApiConfig.getApiService(requireContext())
                val response = apiService.deleteAccount(email)

                if (response.status) {
                    showToastOnMainThread("Link untuk menghapus akun telah dikirim ke email Anda.")
                    userPreference.logout()
                    navigateToWelcome()
                } else {
                    showToastOnMainThread(response.message ?: "Gagal menghapus akun.")
                }
            } catch (e: Exception) {
                showToastOnMainThread("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun showToastOnMainThread(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToWelcome() {
        lifecycleScope.launch(Dispatchers.Main) {
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
