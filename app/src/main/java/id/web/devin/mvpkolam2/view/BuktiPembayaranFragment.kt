package id.web.devin.mvpkolam2.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import id.web.devin.mvpkolam2.presenter.TransaksiDetailPresenter
import id.web.devin.mvpkolam2.databinding.FragmentBuktiPembayaranBinding
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.model.Transaction
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.TransaksiPresenterListener
import id.web.devin.mvpkolam2.util.loadImage

class BuktiPembayaranFragment : Fragment(), TransaksiPresenterListener {
    private lateinit var b: FragmentBuktiPembayaranBinding
    private lateinit var cTransaksi: TransaksiDetailPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cTransaksi = TransaksiDetailPresenter(this.requireContext(),this)
        b = FragmentBuktiPembayaranBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = context?.let { Global.getEmail(it) }
        val sharedPreferences = requireActivity().getSharedPreferences("idtransaksi", Context.MODE_PRIVATE)
        val status = sharedPreferences.getString("status", null)
        val id = sharedPreferences.getString("idtrx", null)
        val role = context?.let { Global.getRole(it) }
        if(role == Role.Admin.name){
            cTransaksi.fetchTransaksiAdminDetail(email.toString(), status.toString(),id.toString())
        }else{
            cTransaksi.fetchTransaksiDetail(email.toString(), status.toString(),id.toString())
        }
        b.btnKembali.setOnClickListener {
            val action = BuktiPembayaranFragmentDirections.actionToPembelianDetailFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun showError(message: String) {
        Log.d("error", message)
    }

    override fun showTransaksi(transaksi: List<Transaction>) {
        transaksi.forEach {
            it.produk.forEach {produk->
                val url = produk.urlBukti
                Log.d("bukti", url)
                b.imgBukti.loadImage(url, b.progressBarBukti)
            }
        }
    }

    override fun success() {}

}