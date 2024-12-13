package com.example.jejak_batik.data.pref

import com.example.jejak_batik.data.api.ModelApiService
import com.example.jejak_batik.data.model.history.HistoryItem
import com.example.jejak_batik.data.model.history.HistoryResponse
import com.example.jejak_batik.data.model.predict.PredictResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ModelRepository(private val apiService: ModelApiService) {

    fun predictImage(email: String, imageFile: File, callback: (PredictResponse?, String?) -> Unit) {
        val emailPart = RequestBody.create("text/plain".toMediaTypeOrNull(), email)

        val imageRequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("img", imageFile.name, imageRequestBody)

        val call = apiService.predict(emailPart, imagePart)
        call.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    fun getHistories(email: String, callback: (List<HistoryItem>?, String?) -> Unit) {
        apiService.getHistories(email).enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val histories = response.body()?.data?.map { item ->
                        HistoryItem(
                            id = item.id,
                            createdAt = item.createdAt,
                            imageUrl = item.imageUrl,
                            result = item.result,
                            data = HistoryItem.NestedData(
                                description = item.data?.description,
                                occasion = item.data?.occasion
                            )
                        )
                    }
                    callback(histories, null)
                } else {
                    callback(null, response.body()?.message ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

}
