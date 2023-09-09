package id.web.devin.mvpkolam2.presenter

import android.util.Log
import id.web.devin.mvpkolam2.model.UploadResponse
import id.web.devin.mvpkolam2.util.UploadPresenterListener
import id.web.devin.mvpkolam2.util.UploadService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UploadPresenter(private val listener:UploadPresenterListener) {

    private val uploadService: UploadService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://lokowai.shop/") // Ganti dengan URL API yang sesuai
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(UploadService::class.java)
    }

    fun uploadImage(imageFile: MultipartBody.Part,folder:RequestBody) {
        val call: Call<UploadResponse> = uploadService.uploadImage(imageFile,folder)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                Log.d("respon", response.toString())
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    Log.d("upload", response.toString())
                    if (uploadResponse != null) {
                        listener.UploadSuccess(uploadResponse)
                    } else {
                        listener.uploadError("Anda belum memilih foto")
                    }
                } else {
                    listener.uploadError("Gagal Mengunggah Bukti")
                }
            }
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                listener.uploadError(t.message.toString())
            }
        })
    }
}