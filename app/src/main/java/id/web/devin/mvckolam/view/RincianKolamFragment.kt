package id.web.devin.mvckolam.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import id.web.devin.mvckolam.R
import id.web.devin.mvckolam.controller.KolamDetailController
import id.web.devin.mvckolam.databinding.FragmentRincianKolamBinding
import id.web.devin.mvckolam.model.Kolam
import id.web.devin.mvckolam.util.KolamDetailControllerListener
import id.web.devin.mvckolam.util.loadImage

class RincianKolamFragment : Fragment(), KolamDetailControllerListener {
    private lateinit var b:FragmentRincianKolamBinding
    private lateinit var cKolamDetail:KolamDetailController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cKolamDetail = KolamDetailController(this.requireContext(),this)
        b = FragmentRincianKolamBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)

        cKolamDetail.fetchDetailKolam(id.toString())
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showKolam(kolam: List<Kolam>) {
        kolam.forEach {
            b.txtNamaKolamRIncian.text = it.nama
            b.txtAlamatKolamRincian.text = it.alamat
            b.txtDeskripsiKolamRincian.text = it.deskripsi
            val peta = it.lokasi
            b.txtPeta.setOnClickListener {
                // Membuka browser dengan URL peta
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(peta))
                startActivity(intent)
            }
            b.imageRincianKolam.loadImage(it.gambarUrl.toString(),b.progressBarRincian)
        }
    }
}