package id.web.devin.mvpkolam2.presenter

import id.web.devin.mvpkolam2.model.*
import id.web.devin.mvpkolam2.util.RajaOngkirService
import id.web.devin.mvpkolam2.util.ShippingPresenterListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShippingPresenter (private val listener: ShippingPresenterListener){

    private val rajaOngkirService = Retrofit.Builder()
        .baseUrl("https://api.rajaongkir.com/starter/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RajaOngkirService::class.java)

    fun fetchShippingCosts(origin: String, destination: String, weight: Int){

//         val apiKey = "f6c65ee25a36a2f2a7767cc130d4e9a6" //devinriyantii
        val apiKey = "0178ae2a9f1df06a92c967cdd512cede" //vinariyantii
        val request = ShippingCostRequest(origin, destination, weight, apiKey, "jne")
        val response = rajaOngkirService.calculateShippingCosts(request)
        response.enqueue(object : Callback<ShippingResponse> {
            override fun onResponse(call: Call<ShippingResponse>, response: Response<ShippingResponse>) {
                if (response.isSuccessful) {
                    val shippingCostResponse = response.body()
                    listener.showShipping(shippingCostResponse)
                } else {
                    listener.showError("Failed to get shipping cost")
                }
            }
            override fun onFailure(call: Call<ShippingResponse>, t: Throwable) {
                listener.showError("Network error: " + t.message)
            }
        })
    }
}