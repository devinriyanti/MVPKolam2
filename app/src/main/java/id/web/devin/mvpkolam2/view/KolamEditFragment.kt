package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvpkolam2.presenter.KolamPresenter
import id.web.devin.mvpkolam2.presenter.KolamDetailPresenter
import id.web.devin.mvpkolam2.databinding.FragmentKolamEditBinding
import id.web.devin.mvpkolam2.model.Kolam
import id.web.devin.mvpkolam2.util.KolamPresenterListener
import id.web.devin.mvpkolam2.util.KolamDetailPresenterListener

class KolamEditFragment : Fragment(), KolamPresenterListener, KolamDetailPresenterListener {
    private lateinit var b: FragmentKolamEditBinding
    private lateinit var cKolam:KolamPresenter
    private lateinit var cKolamDetai:KolamDetailPresenter
    private var namaKolam:String = ""
    private var alamat:String = ""
    private var deskripsi:String = ""
    private var urlLokasi:String = ""
    private var kolamID:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cKolam = KolamPresenter(this.requireContext(),this)
        cKolamDetai = KolamDetailPresenter(this.requireContext(),this)
        b = FragmentKolamEditBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)
        kolamID = id.toString()

        cKolamDetai.fetchDetailKolam(kolamID)

        b.btnEditKolam.setOnClickListener {
            namaKolam = b.editTextNamaKolamEdit.text.toString()
            alamat = b.editTextAlamatKolamEdit.text.toString()
            deskripsi = b.editTextDeskripsiKolamEdit.text.toString()
            urlLokasi = b.editTextLokasiKolamEDit.text.toString()

            if(namaKolam.isNotEmpty() && alamat.isNotEmpty() && deskripsi.isNotEmpty() && urlLokasi.isNotEmpty()){
                cKolam.updateKolam(namaKolam,alamat,deskripsi,urlLokasi,kolamID)
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
        b.btnBatalEditKolam.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Batal Mengubah Data Kolam?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("Batal"){ dialog,_->
                    val action = KolamEditFragmentDirections.actionEditToRincianKolamFragment(kolamID)
                    Navigation.findNavController(it).navigate(action)
                }
                setNegativeButton("Tidak"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }
    }

    override fun showError(message: String) {
        Log.d("error",message)
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Gagal Mengubah Data Kolam")
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

    override fun showKolam(kolam: List<Kolam>) {
        kolam.forEach {
            b.editTextNamaKolamEdit.setText(it.nama)
            b.editTextAlamatKolamEdit.setText(it.alamat)
            b.editTextDeskripsiKolamEdit.setText(it.deskripsi)
            b.editTextLokasiKolamEDit.setText(it.lokasi)
        }
    }

    override fun success() {
        AlertDialog.Builder(context).apply {
            val message = SpannableString("Berhasil Mengubah Data Kolam")
            message.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                message.length,
                0
            )
            setMessage(message)
            setPositiveButton("OK") { _, _ ->
                val action = KolamEditFragmentDirections.actionEditToRincianKolamFragment()
                findNavController().navigate(action)
            }
            create().show()
        }
    }
}