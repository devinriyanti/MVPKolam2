package id.web.devin.mvpkolam2.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvpkolam2.model.*
import id.web.devin.mvpkolam2.util.KolamPresenterListener
import org.json.JSONArray
import org.json.JSONObject

class KolamPresenter(private val context: Context, private val listener: KolamPresenterListener) {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchKolam(cari:String) {
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/kolam.php?cari=$cari"
        val stringReq = StringRequest(Request.Method.GET, url,
        {response->
            val kolam = parseKolam(response)
            Log.d("successKolam", kolam.toString())
            listener.showKolam(kolam)
        },
        {error->
            Log.d("successKolam", error.toString())
            listener.showError(error.toString())
        })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchKolamAdmin(email:String, role:String){
        queue = Volley.newRequestQueue(context)
        var url = "https://lokowai.shop/kolamadmin.php?email=$email&role=$role"
        val stringReq = StringRequest(Request.Method.GET,url,
            { response->
                val kolam = parseKolam(response)
                Log.d("successKolam", kolam.toString())
                listener.showKolam(kolam)
            },
            {error->
                Log.d("successKolam", error.toString())
                listener.showError(error.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun insertKolam(
        nama: String,
        alamat: String,
        deskripsi: String,
        gambar: String,
        lokasi: String,
        email:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/insertkolam.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener{ response ->
                var data = JSONObject(response)
                var status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
                    Log.d("showvolley", response.toString())
                }else{
                    listener.showError("Gagal menambah kolam")
                    Log.d("showError", response.toString())
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
                params["alamat"] = alamat
                params["deskripsi"] = deskripsi
                params["gambar"] = gambar
                params["lokasi"] = lokasi
                params["email_pengguna"] = email
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateKolam(
        nama:String,
        alamat:String,
        deskripsi:String,
        lokasi:String,
        idkolam:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/editkolam.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("editkolam", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
                    Log.d("showSuccess",it.toString())
                }else{
                    listener.showError("Gagal Mengubah Data Kolam")
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
                params["alamat"] = alamat
                params["deskripsi"] = deskripsi
                params["lokasi"] = lokasi
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun removeKolam(idkolam:String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/removekolam.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                Log.d("tes", it)
                if(it.equals("[]")){
                    listener.showError("Kosong")
                }else{
                    val data = JSONObject(it)
                    val status = data.getString("result")
                    if(status.equals("success")){
                        Log.d("showSuccess",it.toString())
                    }else{
                        Log.d("showError",it.toString())
                        listener.showError(it.toString())
                    }
                }
            },
            Response.ErrorListener {
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("removeError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
    fun updateMaintenance(status:Int, idkolam: String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/updatestatusmaintenance.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("statusKolam", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
                    Log.d("showSuccess",it.toString())
                }else{
                    Log.d("showError",it.toString())
                    listener.showError("Gagal Mengubah Status Kolam")
                }
            },
            Response.ErrorListener {
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data",Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["status"] = status.toString()
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
    fun updateStatus(status:Int, idkolam: String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/updatestatuskolam.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("statusKolam", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
                    Log.d("showSuccess",it.toString())
                }else{
                    listener.showError("Gagal Mengubah Status Kolam")
                    Log.d("showError",it.toString())
                }
            },
            Response.ErrorListener {
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                Log.d("updateError", it.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["status"] = status.toString()
                params["idkolam"] = idkolam
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    private fun parseKolam(response: String?): List<Kolam> {
        val kolams = mutableListOf<Kolam>()
        try {
            val jsonArray = JSONArray(response)
            Log.d("jsonkolam", jsonArray.toString())
            for (i in 0 until jsonArray.length()) {
                val kolamJSON: JSONObject = jsonArray.getJSONObject(i)
                val id = kolamJSON.getString("id")
                val nama = kolamJSON.getString("nama")
                val alamat = kolamJSON.getString("alamat")
                val deskripsi = kolamJSON.getString("deskripsi")
                val gambarUrl = kolamJSON.getString("url_gambar")
                val isMaintenance = kolamJSON.getString("is_maintenance")
                val status = kolamJSON.getString("status")
                val kota = kolamJSON.getString("kota")
                val lokasi = kolamJSON.getString("url_lokasi")
                val admin = kolamJSON.getString("email_pengguna")

                val produkArray = kolamJSON.getJSONArray("product")
                val produkList = parseProdukList(produkArray)

                val pelatihArray = kolamJSON.getJSONArray("pelatih")
                val pelatihList = parsePelatihList(pelatihArray)

                val kolam = Kolam(id, nama, alamat, deskripsi, gambarUrl, isMaintenance,status,
                    kota, lokasi, admin, produkList as ArrayList<Produk>, pelatihList as ArrayList<Pelatih>
                )
                kolams.add(kolam)
            }
        } catch (e: Exception) {
            // Handle JSON parsing error
            listener.showError("Tidak Ada Kolam")
        }
        return kolams
    }

    private fun parsePelatihList(array: JSONArray): List<Pelatih> {
        val pelatihList = mutableListOf<Pelatih>()
        try{
            for (i in 0 until array.length()) {
                val pelatihJSON: JSONObject = array.getJSONObject(i)
                val id = pelatihJSON.getString("id")
                val nama = pelatihJSON.getString("nama")
                val tglLahir = pelatihJSON.getString("tanggal_lahir")
                val kontak = pelatihJSON.getString("kontak")
                val tglKarir = pelatihJSON.getString("mulai_karir")
                val deskripsi = pelatihJSON.getString("deskripsi")
                val jenisKelamin = pelatihJSON.getString("jenis_kelamin")
                val gambarUrl = pelatihJSON.getString("url_gambar")

                val pelatih = Pelatih(id, nama, tglLahir, kontak, tglKarir, deskripsi, jenisKelamin, gambarUrl)
                pelatihList.add(pelatih)
            }
        }catch (e: Exception) {
            // Handle JSON parsing error
            listener.showError("Error parsing pelatih")
        }



        return pelatihList
    }

    private fun parseProdukList(array: JSONArray): List<Produk> {
        val produkList = mutableListOf<Produk>()

        for (i in 0 until array.length()) {
            val produkJSON: JSONObject = array.getJSONObject(i)
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
        }
        return produkList
    }
}