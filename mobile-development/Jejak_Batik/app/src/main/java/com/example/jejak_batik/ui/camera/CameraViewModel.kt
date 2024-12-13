package com.example.jejak_batik.ui.camera

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jejak_batik.data.pref.UserPreference
import kotlinx.coroutines.launch

class CameraViewModel(private val userPreference: UserPreference) : ViewModel() {

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> = _imageUri

    private val _motifName = MutableLiveData<String>()
    val motifName: LiveData<String> = _motifName

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
        println("DEBUG: setImageUri -> uri = $uri")
    }

    fun fetchEmail() {
        viewModelScope.launch {
            userPreference.getEmail().collect { email ->
                _email.postValue(email ?: "guest@jejakbatik.com")
                println("DEBUG: fetchEmail -> email = $email")
            }
        }
    }

    fun setMotifDetails(name: String, description: String) {
        _motifName.value = name
        _description.value = description
        println("DEBUG: setMotifDetails -> name = $name, description = $description")
    }


}
