package com.example.jejak_batik.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.jejak_batik.R
import com.example.jejak_batik.data.pref.UserPreference
import com.example.jejak_batik.data.pref.dataStore
import com.example.jejak_batik.databinding.ActivityMainBinding
import com.example.jejak_batik.ui.login.LoginActivity
import com.nafis.bottomnavigation.NafisBottomNavigation
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    lateinit var binding: ActivityMainBinding
    private lateinit var connectivityManager: ConnectivityManager
    private var isInternetDialogShown = false
    private var isNetworkCallbackRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "MainActivity onCreate called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        progressBar = binding.loadingIndicator
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        monitorNetworkChanges()
        if (!isInternetAvailable()) {
            showNoInternetDialog()
            return
        }
        checkUserStatus()
    }

    private fun checkUserStatus() {
        showLoading()
        val userPreference = UserPreference.getInstance(this.dataStore)
        lifecycleScope.launch {
            val user = userPreference.getSession().firstOrNull()
            Log.d("MainActivity", "User session in MainActivity: $user")

            if (user != null && user.isLogin) {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                val bottomNavigation = binding.nafBottomNavigation
                setupBottomNavigation(bottomNavigation, navController)
                Log.d("MainActivity", "Bottom navigation set up for User")
            } else {
                navigateToLogin()
            }
            hideLoading()
        }
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun setupBottomNavigation(bottomNavigation: NafisBottomNavigation, navController: NavController) {
        bottomNavigation.add(NafisBottomNavigation.Model(1, R.drawable.ic_home_black_24dp))
        bottomNavigation.add(NafisBottomNavigation.Model(2, R.drawable.ic_marketplace))
        bottomNavigation.add(NafisBottomNavigation.Model(3, R.drawable.ic_camera))
        bottomNavigation.add(NafisBottomNavigation.Model(4, R.drawable.ic_history))
        bottomNavigation.add(NafisBottomNavigation.Model(5, R.drawable.ic_baseline_person_24))
        bottomNavigation.show(1)
        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> navController.navigate(R.id.navigation_home)
                2 -> navController.navigate(R.id.navigation_catalog)
                3 -> navController.navigate(R.id.navigation_camera)
                4 -> navController.navigate(R.id.navigation_history)
                5 -> navController.navigate(R.id.navigation_setting)
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> bottomNavigation.show(1)
                R.id.navigation_catalog -> bottomNavigation.show(2)
                R.id.navigation_camera -> bottomNavigation.show(3)
                R.id.navigation_history -> bottomNavigation.show(4)
                R.id.navigation_setting -> bottomNavigation.show(5)
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun isInternetAvailable(): Boolean {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    @SuppressLint("NewApi")
    private fun monitorNetworkChanges() {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                runOnUiThread {
                    if (!isInternetDialogShown) {
                        showNoInternetDialog()
                    }
                }
            }
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                runOnUiThread {
                    if (isInternetDialogShown) {
                        isInternetDialogShown = false
                    }
                }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        isNetworkCallbackRegistered = true
    }

    private fun showNoInternetDialog() {
        hideLoading()
        isInternetDialogShown = true
        AlertDialog.Builder(this)
            .setTitle("Tidak Ada Koneksi Internet")
            .setMessage("Harap periksa koneksi internet Anda dan coba lagi.")
            .setPositiveButton("Coba Lagi") { _, _ ->
                if (isInternetAvailable()) {
                    recreate()
                } else {
                    showNoInternetDialog()
                }
            }
            .setNegativeButton("Keluar") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isNetworkCallbackRegistered) {
            try {
                connectivityManager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
            } catch (e: IllegalArgumentException) {
                Log.e("MainActivity", "NetworkCallback was not registered: ${e.message}")
            }
            isNetworkCallbackRegistered = false
        }
    }

}

