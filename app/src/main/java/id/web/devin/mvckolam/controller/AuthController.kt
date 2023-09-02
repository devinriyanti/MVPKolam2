package id.web.devin.mvckolam.controller

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvckolam.util.AuthControllerListener

class AuthController(private val context: Context, private val listener: AuthControllerListener) {
    private val TAG = "volleyTAG"
    private var queue: RequestQueue?= null


    fun loginUser(email:String, pwd:String){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/login.php"
        val stringReq = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Log.d("succesProfil", response.toString())
            },
            Response.ErrorListener {
                Log.d("errorProfil", it.toString())
                listener.showError("Gagal mengambil data pengguna")
            }){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
}