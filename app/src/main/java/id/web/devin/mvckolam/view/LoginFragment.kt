package id.web.devin.mvckolam.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.web.devin.mvckolam.controller.AuthController
import id.web.devin.mvckolam.controller.ProfileController
import id.web.devin.mvckolam.databinding.FragmentLoginBinding
import id.web.devin.mvckolam.model.Pengguna
import id.web.devin.mvckolam.model.Role
import id.web.devin.mvckolam.util.AuthControllerListener
import id.web.devin.mvckolam.util.EncryptionUtils
import id.web.devin.mvckolam.util.Global
import id.web.devin.mvckolam.util.ProfileControllerListener

class LoginFragment : Fragment(), AuthControllerListener, ProfileControllerListener {
    private lateinit var b:FragmentLoginBinding
    private lateinit var cAuth:AuthController
    private lateinit var cProfile: ProfileController
    private lateinit var email:String
    private lateinit var pwd :String
    private lateinit var encryptPwd:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b=FragmentLoginBinding.inflate(layoutInflater)
        cAuth = AuthController(this.requireContext(),this)
        cProfile = ProfileController(this.requireContext(),this)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.progressBarLogin.visibility = View.GONE
        var role = Global.getRole(requireContext())
        if(context?.let { Global.getEmail(it) } != null){
            if(role == "Admin"){
                startActivity(Intent(context, AdminMainActivity::class.java))
            }else{
                startActivity(Intent(context, MainActivity::class.java))
            }
            activity?.finish()
        }

        b.btnLogin.setOnClickListener {
            b.progressBarLogin.visibility = View.VISIBLE
            email = b.editTextEmailLogin.text.toString()
            pwd = b.editTextPwdLogin.text.toString()
            encryptPwd = EncryptionUtils.encrypt(pwd)
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                cAuth.loginUser(email, encryptPwd)
                cProfile.fetchProfil(email)
            }else{
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Email dan Kata Sandi Tidak Boleh Kosong!")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("OK", null)
                    create().show()
                }
            }
        }
    }

    override fun showProfile(profile: List<Pengguna>) {
        b.progressBarLogin.visibility = View.GONE
        profile.forEach {
            val role = it.role
            AlertDialog.Builder(context).apply {
                val sharedPreferences = context.getSharedPreferences(Global.SHARED_PREFERENCES, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(Global.SHARED_PREF_EMAIL, email)
                editor.putString(Global.SHARED_PREF_KEY_ROLE,role.toString())
                editor.apply()
                val nama = it.nama
                val title = SpannableString("Login Berhasil")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Selamat Datang $nama!")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("OK") { _, _ ->
                    val role = it.role
                    if (role == Role.Pengguna) {
                        var intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        var intent = Intent(context, AdminMainActivity::class.java)
                        startActivity(intent)
                    }
                    activity?.finish()
                }
                create().show()
            }
        }
    }

    override fun updateProfil() {
        //
    }

    override fun showError(errorMessage: String) {
        b.txtCekLogin.text = "Email atau Katasandi Salah!"
        b.txtCekLogin.setTextColor(Color.RED)
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

}