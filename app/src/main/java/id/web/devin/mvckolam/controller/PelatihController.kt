package id.web.devin.mvckolam.controller

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvckolam.model.Pelatih
import id.web.devin.mvckolam.util.PelatihControllerListener
import org.json.JSONObject

class PelatihController(val context: Context, private val listener: PelatihControllerListener)  {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null

    fun fetchPelatih(idpelatih:String){
        queue = Volley.newRequestQueue(context)
        var url = "https://lokowai.shop/pelatihdetail.php?id=$idpelatih"
        val stringReq = StringRequest(Request.Method.GET,url,
            {response->
                Log.d("successProduk", response.toString())
                val produk = parsePelatih(response)
                Log.d("success", produk.toString())
                listener.showPelatih(produk)
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

    private fun parsePelatih(response: String?): List<Pelatih> {
        val pelatihList = mutableListOf<Pelatih>()

        val pelatihJSON = JSONObject(response)
        val id = pelatihJSON.getString("id")
        val nama = pelatihJSON.getString("nama")
        val tglLahir = pelatihJSON.getString("tanggal_lahir")
        val kontak = pelatihJSON.getString("kontak")
        val tglKarir = pelatihJSON.getString("mulai_karir")
        val deskripsi = pelatihJSON.getString("deskripsi")
        val jenisKelamin = pelatihJSON.getString("jenis_kelamin")
        val gambarUrl = pelatihJSON.getString("gambar")

        val pelatih = Pelatih(id, nama, tglLahir, kontak, tglKarir, deskripsi, jenisKelamin, gambarUrl)
        pelatihList.add(pelatih)

        return pelatihList
    }
}