package id.web.devin.mvckolam.controller

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvckolam.model.Pengguna
import id.web.devin.mvckolam.model.Produk
import id.web.devin.mvckolam.util.ProductControllerListener
import org.json.JSONObject

class ProductController (val context: Context, private val listener: ProductControllerListener) {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchProduct(idProduk:String){
        queue = Volley.newRequestQueue(context)
        var url = "https://lokowai.shop/productdetail.php?id=$idProduk"
        val stringReq = StringRequest(Request.Method.GET,url,
            {response->
                Log.d("successProduk", response.toString())
                val produk = parseProduk(response)
                Log.d("success", produk.toString())
                listener.showProduk(produk)
            },
            {error->
                Log.d("errorProduk", error.toString())
                listener.showError(error.toString())
            }
        ).apply {
            tag = "TAG"
        }
        queue?.add(stringReq)
    }

    private fun parseProduk(response: String?): List<Produk> {
        val produkList = mutableListOf<Produk>()

        val produkJSON=JSONObject(response)
        val idproduk = produkJSON.getString("id")
        val kolam = produkJSON.getString("idkolam")
        val nama = produkJSON.getString("nama")
        val kota = produkJSON.getString("kota")
        val deskripsi = produkJSON.getString("deskripsi")
        val qty = produkJSON.optInt("kuantitas")
        val harga = produkJSON.optDouble("harga")
        val diskon = produkJSON.optDouble("diskon")
        val gambarUrl = produkJSON.getString("gambar")
        val berat = produkJSON.optDouble("berat")

        val produk = Produk(idproduk, kolam, nama, kota, deskripsi, qty, harga, diskon, gambarUrl, berat)
        produkList.add(produk)

        return produkList
    }
}


