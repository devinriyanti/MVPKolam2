package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import id.web.devin.mvpkolam2.presenter.PelatihPresenter
import id.web.devin.mvpkolam2.databinding.FragmentPelatihDetailBinding
import id.web.devin.mvpkolam2.model.Pelatih
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.PelatihPresenterListener
import id.web.devin.mvpkolam2.util.calculateTotalYears
import id.web.devin.mvpkolam2.util.loadImage

class PelatihDetailFragment : Fragment(), PelatihPresenterListener {
    private lateinit var b:FragmentPelatihDetailBinding
    private lateinit var cPelatih:PelatihPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cPelatih = PelatihPresenter(this.requireContext(), this)
        b = FragmentPelatihDetailBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val role = context?.let { Global.getRole(it) }
        if(role == Role.Admin.name){
            b.btnEditPelatihDetail.visibility = View.VISIBLE
            b.btnHapusPelatih.visibility = View.VISIBLE
        }else{
            b.btnEditPelatihDetail.visibility = View.GONE
            b.btnHapusPelatih.visibility = View.GONE
        }

        if(arguments != null){
            val pelatihID = PelatihDetailFragmentArgs.fromBundle(requireArguments()).pelatihID
            cPelatih.fetchPelatih(pelatihID)

            b.btnEditPelatihDetail.setOnClickListener {
                val action = PelatihDetailFragmentDirections.actionPelatihEditFragment(pelatihID)
                Navigation.findNavController(it).navigate(action)
            }

            b.btnHapusPelatih.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Anda yakin ingin menghapus data pelatih?")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Ya"){ dialog,_->
                        cPelatih.removePelatih(pelatihID )
                        val action = PelatihDetailFragmentDirections.actionPDKolamDetailFragment()
                        findNavController().navigate(action)
                    }
                    setNegativeButton("Tidak"){ dialog,_->
                        dialog.dismiss()
                    }
                    create().show()
                }
            }
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

    override fun succes() {}
}