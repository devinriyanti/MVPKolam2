package id.web.devin.mvpkolam2.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvpkolam2.model.ProductTransaction
import id.web.devin.mvpkolam2.model.StatusTransaksi
import id.web.devin.mvpkolam2.model.Transaction
import id.web.devin.mvpkolam2.util.TransaksiPresenterListener
import org.json.JSONArray
import org.json.JSONObject

class TransaksiPresenter(private val context: Context, private val listener: TransaksiPresenterListener) {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun insertTransaksi(
        ongkir:Int,
        idkeranjang:Int,
        idkolam:String,
        email_pengguna:String,
        idkota:Int,
        alamat:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/transaksi.php"
        val stringReq = object :StringRequest(Method.POST, url,
            Response.Listener { response ->
                Log.d("respon", response)
                var data = JSONObject(response)
                var status = data.getString(("result"))
                if(status.equals("success")){
                    listener.success()
                    Log.d("transaksiVolley",status.toString())
                }else{
                    listener.showError("Gagal Menambah Transaksi")
                    Log.d("transaksiError",response.toString())
                }
            },
            Response.ErrorListener { error->
                Toast.makeText(context,"Kesalahan Saat Menambahkan Produk", Toast.LENGTH_SHORT).show()
                Log.d("showvolley", error.toString())
            }){
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["ongkos_kirim"] = ongkir.toString()
                params["id_keranjangs"] = idkeranjang.toString()
                params["idkolam"] = idkolam
                params["email_pengguna"] = email_pengguna
                params["id_kota"] = idkota.toString()
                params["alamat_kirim"] = alamat
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchTransaksi(email:String, status:String) {
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/transaksilist.php"
        val stringReq = object : StringRequest(Method.POST, url,
            { response ->

                val transaksi = parseTransaksi(response)
                Log.d("successTransaksi", transaksi.toString())
                listener.showTransaksi(transaksi)
            },
            { error ->
                Log.d("errorTransaksi", error.toString())
                listener.showError(error.toString())
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["status"] = status
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun fetchTransaksiAdmin(email:String, status:String) {
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/transaksiadmin.php"
        val stringReq = object : StringRequest(Method.POST, url,
            { response ->
                val transaksi = parseTransaksi(response)
                Log.d("successTransaksi", transaksi.toString())
                listener.showTransaksi(transaksi)
            },
            { error ->
                Log.d("errorTransaksi", error.toString())
                listener.showError(error.toString())
            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["email"] = email
                params["status"] = status
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateStatus(idtransaksi: String, status: String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/updatetransaksi.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateTransaksi", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
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
                params["idtransaksi"] = idtransaksi
                params["status"] = status
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun konfirmasiTransaksi(idtransaksi: String, status: String, no_resi:String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/transaksikonfirmasi.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateTransaksi", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
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
                params["idtransaksi"] = idtransaksi
                params["status"] = status
                params["no_resi"] = no_resi
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun updateBuktiPembayaran(urlBukti:String, idtransaksi: String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/uploadbuktitrf.php"
        val stringReq = object  : StringRequest(Method.POST, url,
            Response.Listener {
                val data = JSONObject(it)
                Log.d("dataUpdateTransaksi", data.toString())
                val status = data.getString("result")
                if(status.equals("success")){
                    listener.success()
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
                params["idtransaksi"] = idtransaksi
                params["urlBukti"] = urlBukti
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    private fun parseTransaksi(response: String?): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        try {
            val jsonArray = JSONArray(response)
            Log.d("jsonTransaksi", jsonArray.toString())

            for (i in 0 until jsonArray.length()) {
                val transactionJSON: JSONObject = jsonArray.getJSONObject(i)
                val id = transactionJSON.getString("id")
                val namaKolam = transactionJSON.getString("namaKolam")
                val email = transactionJSON.getString("email")
                val status = StatusTransaksi.valueOf(transactionJSON.getString("status_transaksi"))

                val produkArray = transactionJSON.getJSONArray("produk")
                val produkList = parseProductTransactionList(produkArray)

                val transaction = Transaction(id, namaKolam, email, "", status,
                    produkList as ArrayList<ProductTransaction>
                )
                transactions.add(transaction)
            }
        } catch (e: Exception) {
            // Handle JSON parsing error
            listener.showError("Tidak Ada Transaksi")
        }
        return transactions
    }

    private fun parseProductTransactionList(array: JSONArray): List<ProductTransaction> {
        val produkList = mutableListOf<ProductTransaction>()

        for (i in 0 until array.length()) {
            val produkJSON: JSONObject = array.getJSONObject(i)
            val id = produkJSON.getString("id")
            val idkolam = produkJSON.getString("idkolam")
            val tanggal = produkJSON.getString("tanggal")
            val total_harga = produkJSON.getDouble("total_harga")
            val alamat_kirim = produkJSON.getString("alamat_kirim")
            val status_transaksi = produkJSON.getString("status_transaksi")
            val urlBukti = produkJSON.getString("urlBukti")
            val tanggalBayar = produkJSON.getString("tanggal_pembayaran")
            val no_resi = produkJSON.getString("no_resi")
            val idkota = produkJSON.getInt("idkota")
            val email_pengguna = produkJSON.getString("email_pengguna")
            val namaKolam = produkJSON.getString("namaKolam")
            val emailAdmin = produkJSON.getString("email")
            val idproduk = produkJSON.getString("idproduk")
            val nama = produkJSON.getString("nama")
            val qty = produkJSON.getInt("qty")
            val harga = produkJSON.getDouble("harga")
            val diskon = produkJSON.getDouble("diskon")
            val berat = produkJSON.getInt("berat")
            val gambar = produkJSON.getString("url_gambar")

            val produk = ProductTransaction(id, idkolam, tanggal, total_harga, alamat_kirim, status_transaksi,
                urlBukti, tanggalBayar, no_resi, idkota, email_pengguna, namaKolam, emailAdmin, idproduk,
                nama, qty, harga, diskon, berat, gambar)
            produkList.add(produk)
        }

        return produkList
    }

}


