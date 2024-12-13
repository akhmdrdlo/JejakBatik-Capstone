package com.example.jejak_batik.ui.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jejak_batik.R
import com.example.jejak_batik.databinding.ActivityWelcomeBinding
import com.example.jejak_batik.ui.login.LoginActivity
import com.example.jejak_batik.ui.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val enterAnimation = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -1000f, 0f).apply {
            duration = 1000L
        }
        enterAnimation.start()
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}


