package id.web.devin.mvpkolam2.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvpkolam2.model.Produk
import id.web.devin.mvpkolam2.util.ProductPresenterListener
import org.json.JSONObject

class ProductPresenter (val context: Context, private val listener: ProductPresenterListener) {
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

    fun insertProduct(
        nama: String,
        deskripsi: String,
        qty: Int,
        harga: Double,
        diskon: Double,
        gambar:String,
        berat:Int,
        idkolam:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/insertproduk.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener{ response ->
                var data = JSONObject(response)
                var status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
                    Log.d("showvolley", response.toString())
                }else{
                    Log.d("showError", response.toString())
                    listener.showError("Gagal menambah produk")
                }
            },
            Response.ErrorListener { error ->
                // Menangani kesalahan
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("showvolley", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["deskripsi"] = deskripsi
                params["kuantitas"] = qty.toString()
                params["harga"] = harga.toString()
                params["diskon"] = diskon.toString()
                params["gambar"] = gambar
                params["berat"] = berat.toString()
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateProduk(
        nama: String,
        deskripsi: String,
        qty: Int,
        harga: Double,
        diskon: Double,
        berat:Int,
        idproduk:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/editproduk.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("editproduk", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
                    Log.d("showSuccess",it.toString())
                }else{
                    listener.showError("Gagal mengubah data produk")
                    Log.d("showError",it.toString())
                }
            },
            Response.ErrorListener {
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["deskripsi"] = deskripsi
                params["kuantitas"] = qty.toString()
                params["harga"] = harga.toString()
                params["diskon"] = diskon.toString()
                params["berat"] = berat.toString()
                params["idproduk"] = idproduk
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateStatus(status:Int, idproduk: String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/updatestatusproduk.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("statusProduk", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    Log.d("showSuccess",it.toString())
                }else{
                    Log.d("showError",it.toString())
                }
            },
            Response.ErrorListener {
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["status"] = status.toString()
                params["idproduk"] = idproduk
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
    fun removeProduk(idproduk:String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/removeproduk.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                Log.d("tes", it)
                if(it.equals("[]")){
                    listener.showError("kosong")
                }else{
                    val data = JSONObject(it)
                    val status = data.getString("result")
                    if(status.equals("success")){
                        Log.d("showSuccess",it.toString())
                    }else{
                        listener.showError(it.toString())
                        Log.d("showError",it.toString())
                    }
                }
            },
            Response.ErrorListener {
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("removeError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["idproduk"] = idproduk
                return params
            }
        }
        stringReq.tag = TAG
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
        val gambarUrl = produkJSON.getString("url_gambar")
        val berat = produkJSON.getInt("berat")
        val status = produkJSON.getString("status")

        val produk = Produk(idproduk, kolam, nama, kota, deskripsi, qty, harga, diskon, gambarUrl, berat,status)
        produkList.add(produk)

        return produkList
    }
}


