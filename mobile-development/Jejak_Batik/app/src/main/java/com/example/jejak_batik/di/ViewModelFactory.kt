package com.example.jejak_batik.di


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jejak_batik.data.pref.ModelRepository
import com.example.jejak_batik.data.pref.UserPreference
import com.example.jejak_batik.data.pref.UserRepository
import com.example.jejak_batik.data.pref.dataStore
import com.example.jejak_batik.ui.camera.CameraViewModel
import com.example.jejak_batik.ui.history.HistoryViewModel
import com.example.jejak_batik.ui.login.LoginViewModel
import com.example.jejak_batik.ui.signup.SignupViewModel
import com.example.jejak_batik.ui.signup.VerifyViewModel
import com.example.jejak_batik.ui.splash.SplashViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val userPreference: UserPreference,
    private val modelRepository: ModelRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(VerifyViewModel::class.java) -> {
                VerifyViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(userPreference) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
                CameraViewModel(userPreference) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(modelRepository, userPreference) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val userRepository = Injection.provideUserRepository(context)
                val userPreference = UserPreference.getInstance(context.dataStore)
                val modelRepository = Injection.provideModelRepository()
                ViewModelFactory(userRepository, userPreference, modelRepository)
            }.also { instance = it }
    }
}
