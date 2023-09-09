package id.web.devin.mvpkolam2.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import id.web.devin.mvpkolam2.presenter.TransaksiPresenter
import id.web.devin.mvpkolam2.databinding.FragmentDibatalkanBinding
import id.web.devin.mvpkolam2.model.Role
import id.web.devin.mvpkolam2.model.StatusTransaksi
import id.web.devin.mvpkolam2.model.Transaction
import id.web.devin.mvpkolam2.util.Global
import id.web.devin.mvpkolam2.util.TransaksiPresenterListener

class DibatalkanFragment : Fragment(), TransaksiPresenterListener {
    private lateinit var b:FragmentDibatalkanBinding
    private lateinit var pembelianListAdapter: PembelianListAdapter
    private lateinit var cTransaksi: TransaksiPresenter
    lateinit var email:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        cTransaksi = TransaksiPresenter(this.requireContext(),this)
        b = FragmentDibatalkanBinding.inflate(layoutInflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        b.txtErrorDibatalkan.visibility = View.GONE
        email = context?.let { Global.getEmail(it) }.toString()
        pembelianListAdapter = PembelianListAdapter(arrayListOf())
        val role = Global.getRole(requireContext())
        if(role == Role.Admin.toString()){
            cTransaksi.fetchTransaksiAdmin(email, StatusTransaksi.Dibatalkan.name)
        }else{
            cTransaksi.fetchTransaksi(email,StatusTransaksi.Dibatalkan.name)
        }
    }

    override fun showError(message: String) {
        if(message == "Tidak Ada Transaksi"){
            b.txtStatusDibatalkan.text = message
        }else{
            b.txtErrorDibatalkan.visibility = View.VISIBLE
        }
        Log.d("eror",message)
        b.progressBarDibatalkan.visibility = View.GONE
    }

    override fun showTransaksi(transaksi: List<Transaction>) {
        transaksi.forEach {
            if(it.id != "null"){
                pembelianListAdapter.updateTransactionList(transaksi)
                b.recViewDibatalkan.layoutManager = LinearLayoutManager(context)
                b.recViewDibatalkan.adapter = pembelianListAdapter
                b.progressBarDibatalkan.visibility = View.GONE
            }else{
                b.txtStatusDibatalkan.text = "Tidak Ada Transaksi"
                b.progressBarDibatalkan.visibility = View.GONE
            }
        }
    }

    override fun success() {}
}