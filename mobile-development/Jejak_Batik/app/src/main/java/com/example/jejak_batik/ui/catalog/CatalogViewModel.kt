package com.example.jejak_batik.ui.catalog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jejak_batik.data.api.ModelApiConfig
import com.example.jejak_batik.data.model.catalog.CatalogItem
import com.example.jejak_batik.data.model.catalog.CatalogResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogViewModel : ViewModel() {
    private val _catalogItems = MutableLiveData<List<CatalogItem>>()
    val catalogItems: LiveData<List<CatalogItem>> get() = _catalogItems

    init {
        fetchCatalogItems()
    }

    private fun fetchCatalogItems() {
        viewModelScope.launch {
            val service = ModelApiConfig.getApiService()
            service.getCatalog().enqueue(object : Callback<CatalogResponse> {
                override fun onResponse(
                    call: Call<CatalogResponse>,
                    response: Response<CatalogResponse>
                ) {
                    if (response.isSuccessful) {
                        val catalogMap = response.body()?.data ?: emptyMap()
                        _catalogItems.value = catalogMap.values.toList()
                    }
                }

                override fun onFailure(call: Call<CatalogResponse>, t: Throwable) {
                }
            })
        }
    }
}
