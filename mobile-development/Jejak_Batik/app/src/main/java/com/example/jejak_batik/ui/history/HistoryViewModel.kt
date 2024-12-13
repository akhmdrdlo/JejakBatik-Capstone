package com.example.jejak_batik.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jejak_batik.data.model.history.HistoryItem
import com.example.jejak_batik.data.pref.ModelRepository
import com.example.jejak_batik.data.pref.UserPreference
import kotlinx.coroutines.launch
import java.io.IOException

class HistoryViewModel(
    private val modelRepository: ModelRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryItem>>()
    val historyList: LiveData<List<HistoryItem>> = _historyList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchHistories(email: String) {
        viewModelScope.launch {
            try {
                modelRepository.getHistories(email) { histories, error ->
                    if (histories != null) {
                        if (histories.isEmpty()) {
                            _historyList.postValue(emptyList())
                            _errorMessage.postValue("")
                        } else {
                            val sortedHistories = histories.sortedByDescending { it.createdAt }
                            _historyList.postValue(sortedHistories)
                            _errorMessage.postValue("")
                        }
                    } else if (error != null) {
                        _errorMessage.postValue(error)
                    }
                }
            } catch (e: IOException) {
                _errorMessage.postValue("Tidak ada koneksi internet.")
            } catch (e: Exception) {
                _errorMessage.postValue("Kesalahan tidak terduga: ${e.message}")
            }
        }
    }

    fun getEmail(): LiveData<String?> {
        val emailLiveData = MutableLiveData<String?>()
        viewModelScope.launch {
            userPreference.getEmail().collect { email ->
                emailLiveData.postValue(email)
            }
        }
        return emailLiveData
    }
}
