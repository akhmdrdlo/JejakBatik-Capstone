package com.example.jejak_batik.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.jejak_batik.ui.main.MainActivity
import com.example.jejak_batik.R
import com.example.jejak_batik.di.ViewModelFactory
import com.example.jejak_batik.ui.main.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.imageView)
        startAnimation(imageView) {
            splashViewModel.getToken().observe(this) { token ->
                if (token.isNullOrEmpty()) {
                    clearUserSession()
                    navigateToWelcome()
                } else {
                    navigateToMain()
                }
            }
        }
    }

    fun clearUserSession() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                splashViewModel.clearSession()
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error clearing session: ${e.message}")
            }
        }
    }

    private fun startAnimation(view: View, onAnimationEnd: () -> Unit) {
        val enterAnimation = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, -1000f, 0f).apply {
            duration = 1000
        }

        val pauseAnimation = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, 0f).apply {
            duration = 2000
        }

        val exitAnimation = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, 1000f).apply {
            duration = 1000
        }

        AnimatorSet().apply {
            playSequentially(enterAnimation, pauseAnimation, exitAnimation)
            start()
            doOnEnd { onAnimationEnd() }
        }
    }

    private fun AnimatorSet.doOnEnd(action: () -> Unit) {
        addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                action()
            }
            override fun onAnimationStart(animation: android.animation.Animator) {}
            override fun onAnimationCancel(animation: android.animation.Animator) {}
            override fun onAnimationRepeat(animation: android.animation.Animator) {}
        })
    }

    private fun navigateToWelcome() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
