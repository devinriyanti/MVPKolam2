package id.web.devin.mvpkolam2.presenter

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvpkolam2.model.*
import id.web.devin.mvpkolam2.util.ProvinsiPresenterListener
import org.json.JSONArray
import org.json.JSONObject

class ProvinsiPresenter (private val context: Context, private val listener:ProvinsiPresenterListener){
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchProvinsi() {
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/kotalist.php"
        val stringReq = StringRequest(
            Request.Method.GET, url,
            {response->
                val provinsi = parseProvinsi(response)
                Log.d("successKolam", provinsi.toString())
                listener.showProvinsi(provinsi)
            },
            {error->
                Log.d("successKolam", error.toString())
                listener.showError(error.toString())
            })
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    private fun parseProvinsi(response: String?): List<Provinsi> {
        val provinsis = mutableListOf<Provinsi>()
        try {
            val jsonArray = JSONArray(response)
            Log.d("jsonkolam", jsonArray.toString())
            for (i in 0 until jsonArray.length()) {
                val kolamJSON: JSONObject = jsonArray.getJSONObject(i)
                val id = kolamJSON.getString("idprovinsi")
                val nama = kolamJSON.getString("nama")

                val kotaArray = kolamJSON.getJSONArray("kota")
                val kotaList = parseKotaList(kotaArray)

                val provinsi = Provinsi(id.toInt(), nama, kotaList as ArrayList<Kota>)
                provinsis.add(provinsi)
            }
        } catch (e: Exception) {
            // Handle JSON parsing error
            listener.showError("Tidak Ada Daftar Provinsi")
        }
        return provinsis
    }

    private fun parseKotaList(array: JSONArray): List<Kota> {
        val kotaList = mutableListOf<Kota>()

        for (i in 0 until array.length()) {
            val tiketJSON: JSONObject = array.getJSONObject(i)
            val idkota = tiketJSON.getString("idkota")
            val nama = tiketJSON.getString("nama_kota")
            val tipe = tiketJSON.getString("type")
            val kode_pos = tiketJSON.getString("kode_pos")

            val kota = Kota(idkota.toInt(), nama, tipe, kode_pos)
            kotaList.add(kota)
        }

        return kotaList
    }
}