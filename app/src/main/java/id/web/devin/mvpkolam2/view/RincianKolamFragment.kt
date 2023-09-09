package id.web.devin.mvpkolam2.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import id.web.devin.mvpkolam2.presenter.KolamPresenter
import id.web.devin.mvpkolam2.presenter.KolamDetailPresenter
import id.web.devin.mvpkolam2.databinding.FragmentRincianKolamBinding
import id.web.devin.mvpkolam2.model.Kolam
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.KolamPresenterListener
import id.web.devin.mvpkolam2.util.KolamDetailPresenterListener
import id.web.devin.mvpkolam2.util.loadImage

class RincianKolamFragment : Fragment(), KolamDetailPresenterListener,KolamPresenterListener {
    private lateinit var b:FragmentRincianKolamBinding
    private lateinit var cKolamDetail:KolamDetailPresenter
    private lateinit var cKolam:KolamPresenter
    private var role:String =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cKolamDetail = KolamDetailPresenter(this.requireContext(),this)
        cKolam = KolamPresenter(this.requireContext(),this)
        b = FragmentRincianKolamBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getSharedPreferences("kolam", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)

        role = context?.let { Global.getRole(it) }.toString()
        if(role == Role.Admin.name){
            b.btnEditKolamRincian.visibility = View.VISIBLE
            b.btnHapusKolam.visibility = View.VISIBLE
            b.switchMaintenance.visibility = View.VISIBLE
            b.switchStatusToko.visibility = View.VISIBLE
        }else{
            b.btnEditKolamRincian.visibility = View.GONE
            b.btnHapusKolam.visibility = View.GONE
            b.switchMaintenance.visibility = View.GONE
            b.switchStatusToko.visibility = View.GONE
        }

        b.switchMaintenance.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cKolam.updateMaintenance(0,id.toString())
            } else {
                cKolam.updateMaintenance(1,id.toString())
            }
        }

        b.switchStatusToko.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(context).apply {
                    val title = SpannableString("Peringatan")
                    title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                    val message = SpannableString("Anda yakin ingin menutup kolam secara permanen?")
                    message.setSpan(
                        AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0,
                        message.length,
                        0
                    )
                    setTitle(title)
                    setMessage(message)
                    setPositiveButton("Ya"){ dialog,_->
                        cKolam.updateStatus(1,id.toString())
                    }
                    setNegativeButton("Tidak"){ dialog,_->
                        dialog.dismiss()
                    }
                    create().show()
                }
            } else {
                cKolam.updateStatus(0,id.toString())
            }
        }

        b.btnEditKolamRincian.setOnClickListener {
            val action = RincianKolamFragmentDirections.actionKolamEditFragment()
            Navigation.findNavController(it).navigate(action)
        }

        b.btnHapusKolam.setOnClickListener {
            AlertDialog.Builder(context).apply {
                val title = SpannableString("Peringatan")
                title.setSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, title.length, 0)
                val message = SpannableString("Anda yakin ingin menghapus data kolam?")
                message.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    message.length,
                    0
                )
                setTitle(title)
                setMessage(message)
                setPositiveButton("Ya"){ dialog,_->
                    cKolam.removeKolam(id.toString())
                    val intent = Intent(context,AdminMainActivity::class.java)
                    startActivity(intent)
                }
                setNegativeButton("Tidak"){ dialog,_->
                    dialog.dismiss()
                }
                create().show()
            }
        }

        cKolamDetail.fetchDetailKolam(id.toString())
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showKolam(kolam: List<Kolam>) {
        kolam.forEach {
            b.switchMaintenance.isChecked = it.is_maintenance.equals("0")
            b.switchStatusToko.isChecked = it.status.equals("1")
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

    override fun success() {}
}