package id.web.devin.mvckolam.controller

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvckolam.model.ProductTransaction
import id.web.devin.mvckolam.model.StatusTransaksi
import id.web.devin.mvckolam.model.Transaction
import id.web.devin.mvckolam.util.TransaksiControllerListener
import org.json.JSONArray
import org.json.JSONObject

class TransaksiController(private val context: Context, private val listener: TransaksiControllerListener) {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

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
            val berat = produkJSON.getDouble("berat")
            val gambar = produkJSON.getString("gambar")

            val produk = ProductTransaction(id, idkolam, tanggal, total_harga, alamat_kirim, status_transaksi,
                urlBukti, tanggalBayar, no_resi, idkota, email_pengguna, namaKolam, emailAdmin, idproduk,
                nama, qty, harga, diskon, berat, gambar)
            produkList.add(produk)
        }

        return produkList
    }

}


