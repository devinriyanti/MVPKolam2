package id.web.devin.mvckolam.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import id.web.devin.mvckolam.controller.PelatihController
import id.web.devin.mvckolam.databinding.FragmentPelatihDetailBinding
import id.web.devin.mvckolam.model.Pelatih
import id.web.devin.mvckolam.util.PelatihControllerListener
import id.web.devin.mvckolam.util.calculateTotalYears
import id.web.devin.mvckolam.util.loadImage

class PelatihDetailFragment : Fragment(), PelatihControllerListener {
    private lateinit var b:FragmentPelatihDetailBinding
    private lateinit var cPelatih:PelatihController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cPelatih = PelatihController(this.requireContext(), this)
        b = FragmentPelatihDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null){
            val pelatihID = PelatihDetailFragmentArgs.fromBundle(requireArguments()).pelatihID
            cPelatih.fetchPelatih(pelatihID)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    override fun showPelatih(pelatih: List<Pelatih>) {
        pelatih.forEach {
            val thnPengalaman = calculateTotalYears(it.tglKarir.toString())
            val umur = calculateTotalYears(it.tglLahir.toString())
            b.txtNamaPelatihDetail.text = it.nama
            b.txtUmurPelatihDetail.text = "${umur} Tahun"
            b.txtKontakPelatihDetail.text = it.kontak
            b.txtPengalamanPelatihDetail.text = "${thnPengalaman} Tahun"
            b.txtDeskripsiPelatihDetail.text = it.deskripsi
            b.imagePelatihDetail.loadImage(it.gambarUrl.toString(),b.progressBarPelatihDetail)
        }
    }
}