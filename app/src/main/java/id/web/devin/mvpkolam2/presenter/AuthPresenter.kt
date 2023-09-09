package id.web.devin.mvpkolam2.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import id.web.devin.mvpkolam2.util.AuthPresenterListener
import org.json.JSONObject

class AuthPresenter(private val context: Context, private val listener: AuthPresenterListener) {
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

    fun registerUser(
        nama: String,
        email: String,
        alamat:String,
        noTelp: String,
        pwd: String,
        role: String,
        idkota:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/registrasi.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener{ response ->
                var data = JSONObject(response)
                var status = data.getString("result")
                if(status.equals("success")){
                    listener.register()
                    Log.d("showvolley", response.toString())
                }else{
                    listener.showError("Gagal Melakukan Registrasi")
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
                params["email"] = email
                params["alamat"] = alamat
                params["telepon"] = noTelp
                params["password"] = pwd
                params["role"] = role
                params["idkota"] = idkota
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }

    fun registerAdmin(
        nama: String,
        email: String,
        alamat:String,
        norekening:String,
        namarekening:String,
        noTelp: String,
        pwd: String,
        role: String,
        idkota:String
    ){
        queue = Volley.newRequestQueue(context)
        val url = "https://lokowai.shop/registrasiadmin.php"
        val stringReq = object : StringRequest(Method.POST, url,
            Response.Listener{ response ->
                var data = JSONObject(response)
                var status = data.getString("result")
                if(status.equals("success")){
                    listener.register()
                    Log.d("showvolley", response.toString())
                }else{
                    listener.showError("Gagal Melakukan Registrasi")
                    Log.d("showError", response.toString())
                }
            },
            Response.ErrorListener { error ->
                // Menangani kesalahan
                Toast.makeText(context,"Kesalahan Saat Mengakses Basis Data", Toast.LENGTH_SHORT).show()
                listener.showError("Kesalahan Saat Mengakses Basis Data")
                Log.d("showvolley", error.toString())
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["nama"] = nama
                params["email"] = email
                params["alamat"] = alamat
                params["norekening"] = norekening
                params["nama_rekening"] = namarekening
                params["telepon"] = noTelp
                params["password"] = pwd
                params["role"] = role
                params["idkota"] = idkota
                return params
            }
        }
        stringReq.tag = TAG
        queue?.add(stringReq)
    }
}

