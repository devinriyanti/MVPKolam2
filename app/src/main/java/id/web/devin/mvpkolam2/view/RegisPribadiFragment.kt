package id.web.devin.mvpkolam2.view

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import id.web.devin.mvpkolam2.presenter.AuthPresenter
import id.web.devin.mvpkolam2.presenter.ProvinsiPresenter
import id.web.devin.mvpkolam2.databinding.FragmentRegisPribadiBinding
import id.web.devin.mvpkolam2.model.Provinsi
import id.web.devin.mvpkolam2.util.AuthPresenterListener
import id.web.devin.mvpkolam2.util.EncryptionUtils
import id.web.devin.mvpkolam2.util.ProvinsiPresenterListener

class RegisPribadiFragment : Fragment() , AuthPresenterListener, ProvinsiPresenterListener{
    private lateinit var b:FragmentRegisPribadiBinding
    private lateinit var cAuth: AuthPresenter
    private lateinit var cProvinsi:ProvinsiPresenter
    private lateinit var nama:String
    private lateinit var email:String
    private lateinit var telepon:String
    private lateinit var alamat:String
    private lateinit var pwd:String
    private lateinit var pwdKonfirmasi:String
    private lateinit var role:String
    private var provinsi:String = ""
    private var selectedKotaId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cAuth = AuthPresenter(this.requireContext(),this)
        cProvinsi = ProvinsiPresenter(this.requireContext(),this)
        b = FragmentRegisPribadiBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.txtMasukPribaditoLogin.setOnClickListener {
            val action = RegisPribadiFragmentDirections.actionRPLoginFragment()
            Navigation.findNavController(it).navigate(action)
        }
        cProvinsi.fetchProvinsi()
    }

    override fun showError(message: String) {
        Log.d("error",message)
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Melakukan Registrasi")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK", null)
            create().show()
        }
    }

    override fun showProvinsi(provinsiList: List<Provinsi>) {
        // Extract nama provinsi dari list provinsi
        val namaProvinsiList = mutableListOf("--Pilih Provinsi--")
        namaProvinsiList.addAll(provinsiList.map { it.nama })

        // Set Spinner dengan data nama provinsi
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaProvinsiList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        b.spinnerProvinsi.adapter = adapter

        b.spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    Toast.makeText(requireContext(), "Silakan pilih provinsi yang valid.", Toast.LENGTH_SHORT).show()
                }else{
                    provinsi = namaProvinsiList[position]

                    // Ambil data kota dari provinsi yang dipilih
                    val kotaList = provinsiList[position].kota

                    // Extract nama kota dari list kota
                    val namaKotaList = mutableListOf("--Pilih Kota--")
                    namaKotaList.addAll(kotaList.map { it.nama })

                    // Set Spinner kota dengan data nama kota
                    val kotaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, namaKotaList)
                    kotaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    b.spinnerKota.adapter = kotaAdapter

                    b.spinnerKota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            // Mengambil ID kota yang dipilih dari list kota
                            selectedKotaId = kotaList[position].idkota
                            b.btnDaftarPribadi.setOnClickListener {
                                nama = b.txtNamaPribadi.text.toString()
                                email = b.txtEmailPribadi.text.toString()
                                pwd = b.txtKSPribadi.text.toString()
                                val encryptPwd = EncryptionUtils.encrypt(pwd)
                                pwdKonfirmasi = b.txtKonKSPribadi.text.toString()
                                telepon = b.txtNoTeleponPribadi.text.toString()
                                alamat = b.txtAlamatPribadi.text.toString()
                                role = "Pengguna"

                                if(nama.isNotEmpty() && email.isNotEmpty() && pwd.isNotEmpty() && telepon.isNotEmpty() && pwdKonfirmasi.isNotEmpty()&& alamat.isNotEmpty()){
                                    if(pwd.equals(pwdKonfirmasi)){
                                        if(pwd.length >= 4 && pwd.length <=8){
                                            cAuth.registerUser(nama, email, alamat ,telepon, encryptPwd, role, selectedKotaId.toString())
                                        }else{
                                            Toast.makeText(context, "Kata Sandi Harus Mengandung 4-8 Karakter!", Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        AlertDialog.Builder(context).apply {
                                            val message = SpannableString("Kata Sandi Tidak Cocok Dengan Konfimasi Kata Sandi")
                                            message.setSpan(
                                                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                                0,
                                                message.length,
                                                0
                                            )
                                            setMessage(message)
                                            setPositiveButton("OK", null)
                                            create().show()
                                        }
                                    }
                                }else{
                                    AlertDialog.Builder(context).apply {
                                        val message = SpannableString("Data Tidak Boleh Kosong!")
                                        message.setSpan(
                                            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                                            0,
                                            message.length,
                                            0
                                        )
                                        setMessage(message)
                                        setPositiveButton("OK", null)
                                        create().show()
                                    }
                                }
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun register() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Melakukan Registrasi")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                var intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }
            create().show()
        }
    }

}