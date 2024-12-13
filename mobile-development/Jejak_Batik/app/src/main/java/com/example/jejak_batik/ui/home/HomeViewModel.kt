package com.example.jejak_batik.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Menggunakan View Model Catalog"
    }
    val text: LiveData<String> = _text
}