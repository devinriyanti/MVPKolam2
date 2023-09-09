package id.web.devin.mvpkolam2.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import id.web.devin.mvpkolam2.presenter.ProfilePresenter
import id.web.devin.mvpkolam2.databinding.FragmentProfileBinding
import id.web.devin.mvpkolam2.model.Pengguna
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.ProfilePresenterListener
import id.web.devin.mvpkolam2.util.formatDate

class ProfileFragment : Fragment(), ProfilePresenterListener {
    private lateinit var b:FragmentProfileBinding
    private lateinit var cProfile:ProfilePresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        b = FragmentProfileBinding.inflate(layoutInflater)
        cProfile = ProfilePresenter(this.requireContext(),this)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = context?.let { Global.getEmail(it) }.toString()
        cProfile.fetchProfil(email)
        b.btnEditProfil.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfilEditFragment()
            Navigation.findNavController(it).navigate(action)
        }
        b.btnLogout.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Apakah Anda Yakin Akan Keluar?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("YA"){ _,_->
                    requireActivity().getSharedPreferences(Global.SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                        .edit()
                        .remove(Global.SHARED_PREF_EMAIL).apply()
                    requireActivity().getSharedPreferences(Global.SHARED_PREFERENCES, Activity.MODE_PRIVATE)
                        .edit()
                        .remove(Global.SHARED_PREF_KEY_ROLE).apply()
                    requireActivity().finish()
                    startActivity(Intent(requireActivity(),LoginActivity::class.java))
                }
                setNegativeButton("TIDAK"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
        b.btnUbahPwd.setOnClickListener {
            val action = ProfileFragmentDirections.actionPasswordEditFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun showProfile(profile: List<Pengguna>) {
        Log.d("profil",profile.toString())
        b.progressBarProfil.visibility = View.GONE
        profile.forEach {
            //Nama
            if(!it.nama.equals("null")){
                b.txtNamaProfil.setText(it.nama)
            }else{
                b.txtNamaProfil.setText("Belum diatur")
            }

            //Email
            if(!it.email.equals("null")){
                b.txtEmailProfil.setText(it.email)
            }else{
                b.txtEmailProfil.setText("Belum diatur")
            }

            //Telepon
            if(!it.telepon.equals("null")){
                b.txtTeleponProfil.setText(it.telepon)
            }else{
                b.txtTeleponProfil.setText("Belum diatur")
            }

            //Alamat
            if(!it.alamat.equals("null")){
                b.txtAlamatProfil.setText(it.alamat)
            }else{
                b.txtAlamatProfil.setText("Belum diatur")
            }

//            Log.d("JenisK",it.jenis_kelamin.displayText)
            //Jenis Kelamin
            if(!it.jenis_kelamin.equals("null")){
                b.txtGenderProfil.setText(it.jenis_kelamin)
            }else{
                b.txtGenderProfil.setText("Belum diatur")
            }

            //Tanggal Lahir
            if(!it.tglLahir.equals("null")){
                val tgl = formatDate(it.tglLahir.toString())
                b.txtTanggalLahir.setText(tgl)
            }else{
                b.txtTanggalLahir.setText("Belum diatur")
            }
        }
    }

    override fun success() {}

    override fun showError(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

}